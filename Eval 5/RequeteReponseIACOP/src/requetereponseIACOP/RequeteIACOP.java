package requetereponseIACOP;

import database.utilities.Bean_DB_Access;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import reponserequetemonothread.Reponse;
import reponserequetemonothread.Requete;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doublon
 */
public class RequeteIACOP implements Requete, Serializable
{
    public final static int REQUEST_LOGIN_GROUP = 1;
    
    private int Type;
    private HashMap<String, Object> chargeUtile;
    private Socket SocketClient;
    
    private ReponseIACOP Reponse = null;
    private Properties Prop = null;

    public RequeteIACOP(int Type, HashMap<String, Object> chargeUtile)
    {
        setType(Type);
        setChargeUtile(chargeUtile);
    }

    public RequeteIACOP(int Type)
    {
        setType(Type);
        setChargeUtile(new HashMap<>());
    }
 
    @Override
    public Runnable createRunnable(Properties Prop)
    {
        setProp(Prop);
        
        switch(getType())
        {
            case REQUEST_LOGIN_GROUP:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoginGroup();
                    }            
                };
                  
            default : return null;
        }
    }

    private void traiteRequeteLoginGroup()     
    {
        String user = getChargeUtile().get("Login").toString();
        long Temps = (long) getChargeUtile().get("Temps");
        double Random = (double) getChargeUtile().get("Random");
        byte[] msgD = (byte[]) getChargeUtile().get("Digest");

        // JBDC
        String[] Champs = ChercheMotdePasse(user);
        if (Champs != null) 
        {
            MessageDigest md;
            try 
            {
                Security.addProvider(new BouncyCastleProvider());  
                md = MessageDigest.getInstance("SHA-256", "BC");                    
                md.update(user.getBytes());
                md.update(Champs[0].getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(Temps);
                bdos.writeDouble(Random);
                md.update(baos.toByteArray());
                byte[] msgDLocal = md.digest();
                
                if (MessageDigest.isEqual(msgD, msgDLocal))  
                {
                    if(Integer.parseInt(Champs[3]) > 0)
                    {
                        Reponse = new ReponseIACOP(ReponseIACOP.LOGIN_OK);
                        Reponse.getChargeUtile().put("Message", ReponseIACOP.LOGIN_OK_MESSAGE);
                        Reponse.getChargeUtile().put("Nom", Champs[1]);
                        Reponse.getChargeUtile().put("Prenom", Champs[2]);
                        Reponse.getChargeUtile().put("PortFly", getProp().getProperty("PORT_FLY"));
                    }
                    else
                    {
                        // Pas de billets commandés
                        Reponse = new ReponseIACOP(ReponseIACOP.LOGIN_KO);
                        Reponse.getChargeUtile().put("Message", ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);                        
                        System.out.println(ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);
                    }
                }
                else 
                {
                    // Tentative de baisage
                    Reponse = new ReponseIACOP(ReponseIACOP.LOGIN_KO);
                    Reponse.getChargeUtile().put("Message", ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);                        
                    System.out.println(ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);
                }
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) 
            {
                Reponse = new ReponseIACOP(ReponseIACOP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE);
            }; 
        }       
        else
        {
            Reponse = new ReponseIACOP(ReponseIACOP.LOGIN_KO);
            Reponse.getChargeUtile().put("Message", ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);                        
            System.out.println(ReponseIACOP.WRONG_USER_PASSWORD_MESSAGE);
        }           
    }
    
    private String[] ChercheMotdePasse(String user) 
    {        
        Bean_DB_Access BD_airport;
        ResultSet RS;
        String[] Champs = null;
        BD_airport = Connexion_DB();
        if (BD_airport != null)
        {
            try 
            {                        
                RS = BD_airport.Select("SELECT (SELECT COUNT(*) FROM bd_airport.billets WHERE bd_airport.billets.IdClient = c.IdClient) NbBillets, Password, Nom, Prenom FROM bd_airport.clients c WHERE Login = \"" + user + "\"");
                if (RS != null) 
                {
                    if(RS.next())
                    {
                        Champs = new String[4];
                        Champs[0] = RS.getString("Password");  
                        Champs[1] = RS.getString("Nom");
                        Champs[2] = RS.getString("Prenom");
                        Champs[3] = Integer.toString(RS.getInt("NbBillets"));  
                    }
                }            
            } 
            catch (SQLException ex) 
            {            
                Reponse = new ReponseIACOP(ReponseIACOP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + ex.getMessage());
            }
        }
        
        BD_airport.Deconnexion();
                
        return Champs;
    }

    public Bean_DB_Access Connexion_DB()
    {
        Bean_DB_Access BD_airport;
        String Error;
        //BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, getProp().getProperty("HOST_BD"), getProp().getProperty("PORT_BD"), "Zeydax", "1234", getProp().getProperty("SCHEMA_BD"));
        BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
        
        if (BD_airport != null)
        {
            Error = BD_airport.Connexion();
            if (Error != null)
            {
                Reponse = new ReponseIACOP(ReponseIACOP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseIACOP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + Error);
            }                
        }        
        
        return BD_airport;
    }
    
    @Override
    public String getNomTypeRequete() 
    {
        switch(getType()) 
        {
            case REQUEST_LOGIN_GROUP: return "REQUEST_LOGIN_GROUP";      
            default : return "UNKNOWN REQUEST";
        }
    }
    
    public int getType() 
    {
        return Type;
    }

    public void setType(int Type) 
    {
        this.Type = Type;
    }

    @Override
    public ReponseIACOP getReponse()
    {
        return Reponse;
    }

    public void setReponse(ReponseIACOP Reponse)
    {
        this.Reponse = Reponse;
    }

    @Override
    public HashMap getChargeUtile() 
    {
        return chargeUtile;
    }
    
    public Properties getProp() 
    {
        return Prop;
    }

    public void setProp(Properties Prop) 
    {
        this.Prop = Prop;
    }

    public void setChargeUtile(HashMap chargeUtile) 
    {
        this.chargeUtile = chargeUtile;
    }
}
