/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import abstracts.ARequete;
import database.utilities.Bean_DB_Access;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
import interfaces.ConsoleServeur;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP extends ARequete {
    public final static int REQUEST_LOAD_FLIGHTS = 2;
    public final static int REQUEST_LOAD_LUGAGES = 3;
    public final static int REQUEST_SAVE_LUGAGES = 4;
                
    public RequeteLUGAP(int Type, HashMap chargeUtile)
    {
        super(Type, chargeUtile);
    }
    
    public RequeteLUGAP(int Type)
    {
        super(Type);
    }
    
    @Override
    public Runnable createRunnable(Socket socket, ConsoleServeur GUI) 
    {
        //setProp(Prop);
        this.setSocketClient(socket);
        this.setGUI(GUI);
        
        return new Runnable() 
        {
            @Override        
            public void run() 
            {
                switch(getType())
                {
                    case REQUEST_LOG_OUT:
                        TraiterRequeteLogOutPorter();
                        break;
                    case REQUEST_LOGIN:
                        TraiterRequeteLoginPorter();
                        break;
                    case REQUEST_LOAD_FLIGHTS:
                        TraiterRequeteLoadFlights();
                        break;
                    case REQUEST_LOAD_LUGAGES:
                        TraiterRequeteLoadLugages();
                        break;
                    case REQUEST_SAVE_LUGAGES:
                        TraiterRequeteSaveLugages();
                        break;
                    default : break; // traiterUnknownRequestOrFail()
                }
                
                GUI.TraceEvenements(SocketClient.getRemoteSocketAddress().toString() + "#" + Reponse.getChargeUtile().get("Message") + "#" + Thread.currentThread().getName());
                EnvoyerReponse();
            }
        };
    }    
    
    private void TraiterRequeteLogOutPorter() 
    {
        Reponse = new ReponseLUGAP(ReponseLUGAP.LOG_OUT_OK);
        Reponse.getChargeUtile().put("Message", ReponseLUGAP.LOG_OUT_OK_MESSAGE);
        
        EnvoyerReponse();
    }
    
    private void TraiterRequeteLoginPorter() 
    {        
        String user = getChargeUtile().get("Login").toString();
        long Temps = (long) getChargeUtile().get("Temps");
        double Random = (double) getChargeUtile().get("Random");
        byte[] msgD = (byte[]) getChargeUtile().get("Digest");

        // JBDC
        String[] Champs = ChercherMotdePasse(user);
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
    
    private String[] ChercherMotdePasse(String user) 
    {        
        Bean_DB_Access BD_airport;
        ResultSet RS;
        String[] Champs = null;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null)
        {
            try 
            {                        
                RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.agents WHERE Poste = \"Bagagiste\" AND Nom = \"Dimartino\"");
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
            
            BD_airport.Deconnexion();
        }        
                
        return Champs;
    }
    
    
    private void TraiterRequeteLoadFlights() 
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
                        
                        hm.put("IdVol", IdVol);
                        hm.put("NumeroVol", NumeroVol);
                        hm.put("NomCompagnie", NomCompagnie);
                        hm.put("Destination", Destination);
                        hm.put("DateHeureDepart", DateHeureDepart);

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
            
            BD_airport.Deconnexion();
        }
        else        
        {
            Reponse = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
            Reponse.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
        }        
    }
    
    private void TraiterRequeteLoadLugages() 
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
    
    private void TraiterRequeteSaveLugages() 
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        int Ok = 0;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null && Reponse == null)
        {            
            for (int i = 1 ; i <= getChargeUtile().size() - 1 ; i++) 
            {
                HashMap<String, Object> hm = (HashMap<String, Object>) getChargeUtile().get(Integer.toString(i));
                
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
        Bean_DB_Access BD_airport = null;
        String Error;
        
        try
        {
            //BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, getProp().getProperty("HOST_BD"), getProp().getProperty("PORT_BD"), "Zeydax", "1234", getProp().getProperty("SCHEMA_BD"));
            BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "123Soleil-", "bd_airport");
        }
        catch(Exception e)
        {
            System.out.println("e = " + e.getMessage());
        }
        
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
    
          
    
    public String getNomTypeRequete() 
    {
        switch(getType()) 
        {
            case REQUEST_LOG_OUT: return "REQUEST_LOG_OUT_PORTER";
            case REQUEST_LOGIN: return "REQUEST_LOGIN_PORTER";                
            case REQUEST_LOAD_FLIGHTS: return "REQUEST_LOAD_FLIGHTS";
            case REQUEST_LOAD_LUGAGES: return "REQUEST_LOAD_LUGAGES";
            case REQUEST_SAVE_LUGAGES: return "REQUEST_SAVE_LUGAGES";
            default : return null;
        }
    }
}
