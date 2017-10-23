/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import database.utilities.Bean_DB_Access_MySQL;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP implements Requete, Serializable{
    //public final static int REQUEST_TEMPORARY_KEY = 0;
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_LOAD_FLIGHTS = 2;
    public final static int REQUEST_LOAD_LUGAGES = 3;
    public final static int REQUEST_SAVE_LUGAGES = 4;
        
    private int Type;
    private HashMap<String, Object> chargeUtile = null;
    private Socket SocketClient;
    
    private ReponseLUGAP Rep = null;

    
    /*public RequeteLUGAP(int Type, HashMap chargeUtile, Socket SocketClient) {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
        this.SocketClient = SocketClient;
    }*/

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
    public Runnable createRunnable(final Socket s, final ConsoleServeur cs) 
    {
        switch(getType())
        {
            case REQUEST_LOGIN_PORTER:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoginPorter(s, cs);
                    }            
                };
            
            case REQUEST_LOAD_FLIGHTS:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoadFlights(s, cs);
                    }            
                };
            
            case REQUEST_LOAD_LUGAGES:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoadLugages(s, cs);
                    }            
                };
            
            case REQUEST_SAVE_LUGAGES:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteSaveLugages(s, cs);
                    }            
                };                
            
            default : return null;
        }
    }    
    
    private void traiteRequeteLoginPorter(Socket s, ConsoleServeur cs) 
    {
        String adresseDistante = s.getRemoteSocketAddress().toString();        
        System.out.println("Debut de traiteRequeteLoginPorter : adresse distante = " + adresseDistante);
        byte b;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        //ReponseLUGAP Rep = null;
        
        
        String user = getChargeUtile().get("Login").toString();
        System.out.println("Utilisateur = " + user);
        long Temps = (long) getChargeUtile().get("Temps");
        //System.out.println("temps = " + Temps);
        double Random = (double) getChargeUtile().get("Random");
        //System.out.println("Nombre aléatoire = " + Random);
        byte[] msgD = (byte[]) getChargeUtile().get("Digest");

        // JBDC
        String Password = ChercheMotdePasse(user);
        if (Password != null) 
        {
            MessageDigest md = null;
            try 
            {
                Security.addProvider(new BouncyCastleProvider());   

                md = MessageDigest.getInstance("SHA-256", "BC");                    
                md.update(user.getBytes());
                md.update(Password.getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(Temps);
                bdos.writeDouble(Random);
                md.update(baos.toByteArray());
                byte[] msgDLocal = md.digest();

                if (MessageDigest.isEqual(msgD, msgDLocal)) 
                {
                    Rep = new ReponseLUGAP(ReponseLUGAP.STATUS_OK);
                    Rep.getChargeUtile().put("Message", ReponseLUGAP.LOGIN_OK);
                    System.out.println(ReponseLUGAP.LOGIN_OK);
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
    
    private String ChercheMotdePasse(String user) 
    {        
        Bean_DB_Access_MySQL BD_airport = null;
        ResultSet RS;
        String Password = null;
        
        try 
        {                        
            BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
            BD_airport.Connexion(); // try catch
            RS = BD_airport.Select("SELECT Password FROM bd_airport.Comptes NATURAL JOIN bd_airport.agents WHERE Poste = \"Bagagiste\" AND Login = \"" + user + "\"");
            if (RS != null) 
            {
                if(RS.next())
                    Password = RS.getString("Password");                    
            }            
        } 
        catch (SQLException ex) 
        {            
            Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
            Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
        }
                
        return Password;
    }
    
    
    private void traiteRequeteLoadFlights(Socket s, ConsoleServeur cs)
    {
        Bean_DB_Access_MySQL BD_airport = null;
        ResultSet RS;
        //ReponseLUGAP Rep = null;
        int i = 1;
         
        BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
        
        try
        {                        
            BD_airport.Connexion(); // try catch
            RS = BD_airport.Select("SELECT bd_airport.vols.IdVol, bd_airport.compagnies.Nom, bd_airport.vols.Destination, bd_airport.vols.HeureDepart FROM bd_airport.vols NATURAL JOIN avions NATURAL JOIN bd_airport.compagnies ORDER BY bd_airport.vols.HeureDepart");
            
            if (RS != null) 
            {         
                Rep = new ReponseLUGAP(ReponseLUGAP.STATUS_OK);
                while(RS.next())
                {
                    int IdVol = RS.getInt("IdVol");
                    System.out.println("IdVol = " + IdVol);
                    String NomCompagnie = RS.getString("Nom");
                    System.out.println("NomCompagnie = " + NomCompagnie);
                    String Destination = RS.getString("Destination");
                    System.out.println("Destination = " + Destination);
                    Timestamp DateHeureDepart = RS.getTimestamp("HeureDepart");
                    System.out.println("DateHeureDepart = " + DateHeureDepart);
                    String HeureDepart = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(DateHeureDepart);
                    System.out.println("HeureDepart = " + HeureDepart);
                    
                    HashMap<String, Object> hm = new HashMap<>();
                    
                    hm.put("IdVol", IdVol);
                    hm.put("NomCompagnie", NomCompagnie);
                    hm.put("Destination", Destination);
                    hm.put("DateDepart", DateHeureDepart);
                    hm.put("HeureDepart", HeureDepart);
                    
                    Rep.getChargeUtile().put(Integer.toString(i), hm);
                    i++;
                } 
                //Rep.getChargeUtile().put("Message", ReponseLUGAP.FLIGHTS_LOADED);
            }
        }
        catch (SQLException ex) 
        {
            Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
            Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
        }
    }
    
    private void traiteRequeteLoadLugages(Socket s, ConsoleServeur cs)
    {
        Bean_DB_Access_MySQL BD_airport = null;
        ResultSet RS;
        int i = 1;
        
        BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
        
        try
        {                        
            BD_airport.Connexion(); // try catch
            RS = BD_airport.Select("SELECT IdBagage, Poids, TypeBagage, Receptionne, Charge, Verifie, Remarques " +
                                    "FROM bd_airport.vols NATURAL JOIN bd_airport.billets NATURAL JOIN bd_airport.bagages " +
                                    "WHERE SUBSTR(IdBillet, 1, 3) = "+ getChargeUtile().get("IdVol") +
                                    " AND SUBSTR(IdBillet, 5, 8) = date_format(sysdate(), '%d%m%Y')");
            if (RS != null) 
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.STATUS_OK);
                while(RS.next())
                {
                    HashMap<String, Object> hm = new HashMap<>();
                    
                    String IdBagage = RS.getString("IdBagage");
                    //System.out.println("IdBagage = " + IdBagage);
                    float Poids = RS.getFloat("Poids");
                    //System.out.println("Poids = " + Poids);
                    String TypeBagage = RS.getString("TypeBagage");
                    //System.out.println("TypeBagage = " + TypeBagage);
                    char Receptionne = RS.getString("Receptionne").charAt(0);
                    //System.out.println("Receptionne = " + Receptionne);
                    char Charge = RS.getString("Charge").charAt(0);
                    //System.out.println("Charge = " + Charge);
                    char Verifie = RS.getString("Verifie").charAt(0);
                    //System.out.println("Verifie = " + Verifie);
                    String Remarques = RS.getString("Remarques");
                    //System.out.println("Remarque = " + Remarque);
                    
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
                //Rep.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_LOADED);
            }
            else
            {
                /*Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);*/
            }
        }
        catch (SQLException ex) 
        {
            Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
            Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
        }           
    }
    
    private void traiteRequeteSaveLugages(Socket s, ConsoleServeur cs)
    {
        Bean_DB_Access_MySQL BD_airport = null;
        ResultSet RS;
        //int i = 1;
        int Ok = 0;
        
        BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
        
        BD_airport.Connexion(); // try catch
        for (int i = 1 ; i <= getChargeUtile().size() ; i++) 
        {
            HashMap<String, Object> hm = (HashMap<String, Object>) getChargeUtile().get(Integer.toString(i));
            System.out.println("UPDATE Bagages "
                        + "SET Receptionne = \"" + hm.get("Receptionne") + "\", Charge = \"" + hm.get("Charge") + "\", Verifie = \"" + hm.get("Verifie") + "\", Remarques = \"" + hm.get("Remarques") 
                        + "\" WHERE IdBagage = \"" + hm.get("Identifiant") + "\"");
            try 
            {
                Ok = BD_airport.Update("UPDATE Bagages "
                        + "SET Receptionne = \"" + hm.get("Receptionne") + "\", Charge = \"" + hm.get("Charge") + "\", Verifie = \"" + hm.get("Verifie") + "\", Remarques = \"" + hm.get("Remarques") 
                        + "\" WHERE IdBagage = \"" + hm.get("Identifiant") + "\"");
                
                System.out.println("Ok = " + Ok);
            } 
            catch (SQLException ex) 
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }
            
            if (Ok == getChargeUtile().size())
            {
                System.out.println("Update ok !");                
                Rep = new ReponseLUGAP(ReponseLUGAP.STATUS_OK);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.LUGAGES_SAVED);
                System.out.println(ReponseLUGAP.LUGAGES_SAVED);
            }    
            else 
            {
                Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }
        } 
    }
    
    /*
    public void EnvoyerReponse(Socket s, ReponseLUGAP Rep)
    {
        ObjectOutputStream oos = null;
        
        try {            
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(Rep); 
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    //Getters - Setters

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
            case REQUEST_LOGIN_PORTER: return "REQUEST_LOGIN_PORTER";                
            case REQUEST_LOAD_FLIGHTS: return "REQUEST_LOAD_FLIGHTS";
            case REQUEST_LOAD_LUGAGES: return "REQUEST_LOAD_LUGAGES";
            case REQUEST_SAVE_LUGAGES: return "REQUEST_SAVE_LUGAGES";
            default : return null;
        }
    }
}
