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
    public final static int REQUEST_LOG_OUT_PORTER = 0;
    public final static int REQUEST_LOGIN_PORTER = 1;
    
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
            case REQUEST_LOG_OUT_PORTER:
                return new Runnable() 
                {
                    public void run() 
                    {
                        //traiteRequeteLogOutPorter();
                    }            
                };
                
            case REQUEST_LOGIN_PORTER:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoginPorter();
                    }            
                };
                  
            default : return null;
        }
    }

    private void traiteRequeteLoginPorter()     
    {
        System.out.println("cc");
        String user = getChargeUtile().get("Login").toString();
        long Temps = (long) getChargeUtile().get("Temps");
        double Random = (double) getChargeUtile().get("Random");
        byte[] msgD = (byte[]) getChargeUtile().get("Digest");

        // JBDC
        System.out.println("cc1");
        String[] Champs = ChercheMotdePasse(user);
        if (Champs != null) 
        {
            MessageDigest md;
            try 
            {
                Security.addProvider(new BouncyCastleProvider());   
                System.out.println("cc2");
                md = MessageDigest.getInstance("SHA-256", "BC");                    
                md.update(user.getBytes());
                md.update(Champs[0].getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(Temps);
                bdos.writeDouble(Random);
                md.update(baos.toByteArray());
                byte[] msgDLocal = md.digest();
                System.out.println("cc3");
                if (MessageDigest.isEqual(msgD, msgDLocal)) 
                {
                    Reponse = new ReponseIACOP(ReponseIACOP.LOGIN_OK);
                    Reponse.getChargeUtile().put("Message", ReponseIACOP.LOGIN_OK_MESSAGE);
                    Reponse.getChargeUtile().put("Nom", Champs[1]);
                    Reponse.getChargeUtile().put("Prenom", Champs[2]);
                }
                else 
                {
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
        System.out.println("mdp");
        BD_airport = Connexion_DB();
        System.out.println("mdp1");
        if (BD_airport != null)
        {
            try 
            {                        
                //RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.agents WHERE Poste = \"Bagagiste\" AND Login = \"" + user + "\"");
                System.out.println("mdp2");
                RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.clients WHERE Login = \"" + user + "\"");
                System.out.println("mdp3");
                if (RS != null) 
                {
                    if(RS.next())
                    {
                        Champs = new String[3];
                        Champs[0] = RS.getString("Password");  
                        Champs[1] = RS.getString("Nom");
                        Champs[2] = RS.getString("Prenom");
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
        System.out.println("co");
        //BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, getProp().getProperty("HOST_BD"), getProp().getProperty("PORT_BD"), "Zeydax", "1234", getProp().getProperty("SCHEMA_BD"));
        BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
        System.out.println("co1");
        
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
            case REQUEST_LOG_OUT_PORTER: return "REQUEST_LOG_OUT_PORTER";
            case REQUEST_LOGIN_PORTER: return "REQUEST_LOGIN_PORTER";                
            //case REQUEST_LOAD_FLIGHTS: return "REQUEST_LOAD_FLIGHTS";
            //case REQUEST_LOAD_LUGAGES: return "REQUEST_LOAD_LUGAGES";
            //case REQUEST_SAVE_LUGAGES: return "REQUEST_SAVE_LUGAGES";
            default : return null;
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
