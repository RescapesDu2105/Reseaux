/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP implements Requete, Serializable{
    //private static final long serialVersionUID = 4296258939822015079L;
    
    public final static int REQUEST_LOG_OUT_PORTER = 0;
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_LOAD_FLIGHTS = 2;
    public final static int REQUEST_LOAD_LUGAGES = 3;
    public final static int REQUEST_SAVE_LUGAGES = 4;
        
    private int Type;
    private HashMap<String, Object> chargeUtile;
    private Socket SocketClient;
    
    private ReponseLUGAP Reponse = null;
    private Properties Prop = null;

    public RequeteLUGAP(int Type, HashMap chargeUtile) 
    {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
    }
    
    public RequeteLUGAP(int Type) 
    {
        this.Type = Type;
        this.chargeUtile = new HashMap<>();
    }
    
    @Override
    public Runnable createRunnable(Properties Prop) 
    //public Runnable createRunnable(HashMap<String, Object> Tab) 
    {
        setProp(Prop);
        
        switch(getType())
        {
            case REQUEST_LOG_OUT_PORTER:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLogOutPorter();
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
            
            case REQUEST_LOAD_FLIGHTS:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoadFlights();//Tab);
                    }            
                };
            
            case REQUEST_LOAD_LUGAGES:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoadLugages();
                    }            
                };
            
            case REQUEST_SAVE_LUGAGES:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteSaveLugages();//Tab);
                    }            
                };                
            
            default : return null;
        }
    }    
    
    private void traiteRequeteLogOutPorter() 
    {
        Reponse = new ReponseLUGAP(ReponseLUGAP.LOG_OUT_OK);
        Reponse.getChargeUtile().put("Message", ReponseLUGAP.LOG_OUT_OK_MESSAGE);
    }
    
    private void traiteRequeteLoginPorter() 
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
                    Reponse = new ReponseLUGAP(ReponseLUGAP.LOGIN_OK);
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.LOGIN_OK_MESSAGE);
                    Reponse.getChargeUtile().put("Nom", Champs[1]);
                    Reponse.getChargeUtile().put("Prenom", Champs[2]);
                }
                else 
                {
                    Reponse = new ReponseLUGAP(ReponseLUGAP.LOGIN_KO);
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
                    System.out.println(ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);
                }
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) 
            {
                Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }; 
        }       
        else
        {
            Reponse = new ReponseLUGAP(ReponseLUGAP.LOGIN_KO);
            Reponse.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
            System.out.println(ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);
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
                RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.agents WHERE Poste = \"Bagagiste\" AND Login = \"" + user + "\"");
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
                Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + ex.getMessage());
            }
        }
        
        BD_airport.Deconnexion();
                
        return Champs;
    }
    
    
    private void traiteRequeteLoadFlights()//HashMap<String, Object> Tab)
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        int i = 1;
         
        BD_airport = Connexion_DB();
        
        if (BD_airport != null)
        {
            try
            {                        
                RS = BD_airport.Select("SELECT bd_airport.vols.IdVol, bd_airport.vols.NumeroVol, bd_airport.compagnies.NomCompagnie, bd_airport.vols.Destination, bd_airport.vols.HeureDepart "
                        + "FROM bd_airport.vols NATURAL JOIN avions NATURAL JOIN bd_airport.compagnies "
                        + "WHERE bd_airport.vols.HeureDepart BETWEEN current_time() AND ADDTIME(current_time(), '04:00:00') "
                        + "AND bd_airport.vols.IdVol IN (SELECT DISTINCT IdVol FROM bd_airport.bagages NATURAL JOIN bd_airport.billets WHERE Charge != 'O') "
                        + "ORDER BY bd_airport.vols.HeureDepart");

                if (RS != null) 
                {         
                    Reponse = new ReponseLUGAP(ReponseLUGAP.FLIGHTS_LOADED);
                    while(RS.next())
                    {
                        int IdVol = RS.getInt("IdVol");
                        int NumeroVol = RS.getInt("NumeroVol");
                        String NomCompagnie = RS.getString("NomCompagnie");
                        String Destination = RS.getString("Destination");
                        Timestamp DateHeureDepart = RS.getTimestamp("HeureDepart");

                        HashMap<String, Object> hm = new HashMap<>();
                        
                        //boolean bContient = Tab.containsValue(IdVol);
                        //System.out.println("Tab = " + Tab);
                        //System.out.println("bContient = " + bContient);
                        //if (!bContient) {
                            hm.put("IdVol", IdVol);
                            hm.put("NumeroVol", NumeroVol);
                            hm.put("NomCompagnie", NomCompagnie);
                            hm.put("Destination", Destination);
                            hm.put("DateHeureDepart", DateHeureDepart);
                        //} hm.put("BagagesChargés", bContient);

                        Reponse.getChargeUtile().put(Integer.toString(i), hm);
                        i++;
                    } 
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.FLIGHTS_LOADED_MESSAGE);
                }
            }
            catch (SQLException ex) 
            {
                if (Reponse == null)
                    Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                else
                    Reponse.setCode(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                
                Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }
        }
        
        BD_airport.Deconnexion();
    }
    
    private void traiteRequeteLoadLugages()
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        int i = 1;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null)
        {
            try
            {                        
                RS = BD_airport.Select("SELECT IdBagage, Poids, TypeBagage, Receptionne, Charge, Verifie, Remarques " +
                                        "FROM bd_airport.vols NATURAL JOIN bd_airport.billets NATURAL JOIN bd_airport.bagages " +
                                        "WHERE bd_airport.vols.IdVol = " + getChargeUtile().get("IdVol"));
                if (RS != null) 
                {
                    Reponse = new ReponseLUGAP(ReponseLUGAP.LUGAGES_LOADED);
                    while(RS.next())
                    {
                        HashMap<String, Object> hm = new HashMap<>();

                        String IdBagage = RS.getString("IdBagage");
                        float Poids = RS.getFloat("Poids");
                        String TypeBagage = RS.getString("TypeBagage");
                        char Receptionne = RS.getString("Receptionne").charAt(0);
                        char Charge = RS.getString("Charge").charAt(0);
                        char Verifie = RS.getString("Verifie").charAt(0);
                        String Remarques = RS.getString("Remarques");

                        hm.put("IdBagage", IdBagage);
                        hm.put("Poids", Poids);
                        hm.put("TypeBagage", TypeBagage);
                        hm.put("Receptionne", Receptionne);
                        hm.put("Charge", Charge);
                        hm.put("Verifie", Verifie);
                        hm.put("Remarques", Remarques);

                        Reponse.getChargeUtile().put(Integer.toString(i), hm);

                        i++;
                    }                
                    Reponse.getChargeUtile().put("IdVol", getChargeUtile().get("IdVol"));
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_LOADED_MESSAGE);
                }
                else
                {
                    if (Reponse == null)
                        Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);

                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                    System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                }
            }
            catch (SQLException ex) 
            {
                Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }     
        }
        
        BD_airport.Deconnexion();
    }
    
    private void traiteRequeteSaveLugages()//HashMap<String, Object> Tab)
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        int Ok = 0;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null && Reponse == null)// || (Reponse != null && !Reponse.getChargeUtile().get("Message").equals(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE)))
        {
            //int IdVol = (int)getChargeUtile().get("IdVol");
            //System.out.println("IdVol = " + IdVol);
            //Tab.put("IdVol", IdVol);
            //Tab.put("Test", "Test");
            //System.out.println("Tab = " + Tab);
            
            //System.out.println("getChargeUtile() = " + getChargeUtile());
            //System.out.println("getChargeUtile().size() = " + getChargeUtile().size());
            
            for (int i = 1 ; i <= getChargeUtile().size() - 1 ; i++) 
            {
                HashMap<String, Object> hm = (HashMap<String, Object>) getChargeUtile().get(Integer.toString(i));
                System.out.println("i = " + i);
                try 
                {
                    Ok = BD_airport.Update("UPDATE Bagages "
                            + "SET Receptionne = \"" + hm.get("Receptionne") + "\", Charge = \"" + hm.get("Charge") + "\", Verifie = \"" + hm.get("Verifie") + "\", Remarques = \"" + hm.get("Remarques") 
                            + "\" WHERE IdBagage = \"" + hm.get("Identifiant") + "\"");
                } 
                catch (SQLException ex) {}

                if (Ok == getChargeUtile().size())
                {     
                    Reponse = new ReponseLUGAP(ReponseLUGAP.LUGAGES_SAVED);
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_SAVED_MESSAGE);
                }    
                else 
                {
                    Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                    Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                }
            } 
        }
        
        BD_airport.Deconnexion();
    }
        
    public Bean_DB_Access Connexion_DB()
    {
        Bean_DB_Access BD_airport;
        String Error;
        
        BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, getProp().getProperty("HOST_BD"), getProp().getProperty("PORT_BD"), "Zeydax", "1234", getProp().getProperty("SCHEMA_BD"));
        //BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
        if (BD_airport != null)
        {
            Error = BD_airport.Connexion();
            if (Error != null)
            {
                Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + Error);
            }                
        }        
        
        return BD_airport;
    }
    
    @Override
    public ReponseLUGAP getReponse() {
        return Reponse;
    }

    public void setReponse(ReponseLUGAP Reponse) {
        this.Reponse = Reponse;
    }
    
    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public HashMap getChargeUtile() {
        return chargeUtile;
    }

    public void setChargeUtile(HashMap chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    public Socket getSocketClient() {
        return SocketClient;
    }

    public void setSocketClient(Socket SocketClient) {
        this.SocketClient = SocketClient;
    }

    public Properties getProp() {
        return Prop;
    }

    public void setProp(Properties Prop) {
        this.Prop = Prop;
    }
    
    public String getNomTypeRequete() 
    {
        switch(getType()) 
        {
            case REQUEST_LOG_OUT_PORTER: return "REQUEST_LOG_OUT_PORTER";
            case REQUEST_LOGIN_PORTER: return "REQUEST_LOGIN_PORTER";                
            case REQUEST_LOAD_FLIGHTS: return "REQUEST_LOAD_FLIGHTS";
            case REQUEST_LOAD_LUGAGES: return "REQUEST_LOAD_LUGAGES";
            case REQUEST_SAVE_LUGAGES: return "REQUEST_SAVE_LUGAGES";
            default : return null;
        }
    }
}
