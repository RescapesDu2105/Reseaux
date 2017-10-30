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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP implements Requete, Serializable{
    //public final static int REQUEST_TEMPORARY_KEY = 0;
    public final static int REQUEST_LOG_OUT_PORTER = 0;
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_LOAD_FLIGHTS = 2;
    public final static int REQUEST_LOAD_LUGAGES = 3;
    public final static int REQUEST_SAVE_LUGAGES = 4;
        
    private int Type;
    private HashMap<String, Object> chargeUtile = null;
    private Socket SocketClient;
    
    private ReponseLUGAP Rep = null;

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
    public Runnable createRunnable() 
    {
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
                        traiteRequeteLoadFlights();
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
                        traiteRequeteSaveLugages();
                    }            
                };                
            
            default : return null;
        }
    }    
    
    private void traiteRequeteLogOutPorter() 
    {
        Rep = new ReponseLUGAP(ReponseLUGAP.LOG_OUT_OK);
        Rep.getChargeUtile().put("Message", ReponseLUGAP.LOG_OUT_OK_MESSAGE);
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
                    Rep = new ReponseLUGAP(ReponseLUGAP.LOGIN_OK);
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.LOGIN_OK_MESSAGE);
                    Rep.getChargeUtile().put("Nom", Champs[1]);
                    Rep.getChargeUtile().put("Prenom", Champs[2]);
                }
                else 
                {
                    Rep = new ReponseLUGAP(ReponseLUGAP.WRONG_USER_PASSWORD);
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
                    System.out.println(ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);
                }
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) 
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }; 
        }       
        else
        {
            Rep = new ReponseLUGAP(ReponseLUGAP.WRONG_USER_PASSWORD);
            Rep.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
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
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + ex.getMessage());
            }
        }
                
        return Champs;
    }
    
    
    private void traiteRequeteLoadFlights()
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
                        + "WHERE bd_airport.vols.HeureDepart BETWEEN current_time() AND ADDTIME(current_time(), '24:00:00') "
                        + "ORDER BY bd_airport.vols.HeureDepart");

                if (RS != null) 
                {         
                    Rep = new ReponseLUGAP(ReponseLUGAP.FLIGHTS_LOADED);
                    while(RS.next())
                    {
                        int IdVol = RS.getInt("IdVol");
                        String NomCompagnie = RS.getString("Nom");
                        String Destination = RS.getString("Destination");
                        Timestamp DateHeureDepart = RS.getTimestamp("HeureDepart");

                        HashMap<String, Object> hm = new HashMap<>();

                        hm.put("IdVol", IdVol);
                        hm.put("NomCompagnie", NomCompagnie);
                        hm.put("Destination", Destination);
                        hm.put("DateHeureDepart", DateHeureDepart);

                        Rep.getChargeUtile().put(Integer.toString(i), hm);
                        i++;
                    } 
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.FLIGHTS_LOADED_MESSAGE);
                }
            }
            catch (SQLException ex) 
            {
                if (Rep == null)
                    Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);

                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }
        }
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
                    Rep = new ReponseLUGAP(ReponseLUGAP.LUGAGES_LOADED);
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

                        Rep.getChargeUtile().put(Integer.toString(i), hm);

                        i++;
                    }                
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_LOADED_MESSAGE);
                }
                else
                {
                    if (Rep == null)
                        Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);

                    Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                    System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                }
            }
            catch (SQLException ex) 
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }     
        }
    }
    
    private void traiteRequeteSaveLugages()
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        int Ok = 0;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null && Rep == null || (Rep != null && !Rep.getChargeUtile().get("Message").equals(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE)))
        {
            for (int i = 1 ; i <= getChargeUtile().size() ; i++) 
            {
                HashMap<String, Object> hm = (HashMap<String, Object>) getChargeUtile().get(Integer.toString(i));
                
                try 
                {
                    Ok = BD_airport.Update("UPDATE Bagages "
                            + "SET Receptionne = \"" + hm.get("Receptionne") + "\", Charge = \"" + hm.get("Charge") + "\", Verifie = \"" + hm.get("Verifie") + "\", Remarques = \"" + hm.get("Remarques") 
                            + "\" WHERE IdBagage = \"" + hm.get("Identifiant") + "\"");

                    System.out.println("Ok = " + Ok);
                } 
                catch (SQLException ex) {}

                if (Ok == getChargeUtile().size())
                {     
                    Rep = new ReponseLUGAP(ReponseLUGAP.LUGAGES_SAVED);
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_SAVED_MESSAGE);
                }    
                else 
                {
                    Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                }
            } 
        }
    }
        
    public Bean_DB_Access Connexion_DB()
    {
        Bean_DB_Access BD_airport;
        String Error;
        
        BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
        if (BD_airport != null)
        {
            Error = BD_airport.Connexion();
            if (Error != null)
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + Error);
            }                
        }        
        
        return BD_airport;
    }
    
    public ReponseLUGAP getRep() {
        return Rep;
    }

    public void setRep(ReponseLUGAP Rep) {
        this.Rep = Rep;
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
