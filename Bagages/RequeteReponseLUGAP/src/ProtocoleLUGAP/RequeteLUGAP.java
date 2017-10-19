/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import database.utilities.Bean_DB_Access_MySQL;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP implements Requete, Serializable{
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_TEMPORARY_KEY = 2;
        
    private int Type;
    private HashMap<String, Object> chargeUtile = null;
    private Socket SocketClient;

    
    public RequeteLUGAP(int Type, HashMap chargeUtile, Socket SocketClient) {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
        this.SocketClient = SocketClient;
    }

    public RequeteLUGAP(int Type, HashMap chargeUtile) {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
    }
    
    public RequeteLUGAP(int Type) {
        this.Type = Type;
        this.chargeUtile = new HashMap<>();
    }
    
    @Override
    public Runnable createRunnable(final Socket s, final ConsoleServeur cs) {
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
            
            default : return null;
        }
    }    
    
    private void traiteRequeteLoginPorter(Socket s, ConsoleServeur cs) {
        String adresseDistante = s.getRemoteSocketAddress().toString();        
        System.out.println("Debut de traiteRequeteLoginPorter : adresse distante = " + adresseDistante);
        byte b;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        ReponseLUGAP Rep = null;
        
        try 
        {
            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
            if (dis == null || dos == null)
                System.err.println("Erreur : dis et/ou dos est/sont null !");
            
            String user = getChargeUtile().get("Login").toString();
            System.out.println("Utilisateur = " + user);
            long Temps = (long) getChargeUtile().get("Temps");
            System.out.println("temps = " + Temps);
            double Random = (double) getChargeUtile().get("Random");
            System.out.println("Nombre aléatoire = " + Random);
            byte[] msgD = (byte[]) getChargeUtile().get("Digest");
            
            // JBDC
            String Password = ChercheMotdePasse(user);
            if (Password != null) {
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
                        Rep.getChargeUtile().put("Message", "OK - vous êtes connecté au serveur !");
                        System.out.println("OK - vous êtes connecté au serveur !");
                    }
                    else 
                    {
                        Rep = new ReponseLUGAP(ReponseLUGAP.WRONG_USER_PASSWORD);
                        Rep.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
                        System.out.println(ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);
                    }
                } 
                catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
                {
                    Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
                }; 
            }       
            else
            {
                /*Rep = new ReponseLUGAP(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);*/
                
                Rep = new ReponseLUGAP(ReponseLUGAP.WRONG_USER_PASSWORD);
                Rep.getChargeUtile().put("Message", ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);                        
                System.out.println(ReponseLUGAP.WRONG_USER_PASSWORD_MESSAGE);
            }
            
            EnvoyerReponse(s, Rep);
            cs.TraceEvenements(s.getRemoteSocketAddress().toString() + "#Envoi de la réponse#" + "Thread client/RequeteLUGAP");
        } 
        catch (IOException ex) {            
            System.err.println("Erreur de flux !");
        }    
                
    }
    
    private String ChercheMotdePasse(String user) {        
        Bean_DB_Access_MySQL BD_airport = null;
        ResultSet RS;
        String Password = null;
        
        try {  
            BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
        }
        catch (Exception ex){
           ex.printStackTrace();
        }
            
        try {                        
            BD_airport.Connexion(); // try catch
            RS = BD_airport.Select("SELECT Password FROM bd_airport.Comptes WHERE Login = \"" + user + "\"");
            if (RS != null) {
                if(RS.next())
                    Password = RS.getString("Password");                    
            }            
        } catch (SQLException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return Password;
    }
    
    public void EnvoyerReponse(Socket s, ReponseLUGAP Rep)
    {
        ObjectOutputStream oos = null;
        
        try {            
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(Rep); 
            oos.flush();
            //oos.close()
        } catch (IOException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Getters - Setters
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
}
