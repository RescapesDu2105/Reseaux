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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class RequeteLUGAP implements Requete, Serializable{
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_TEMPORARY_KEY = 2;
    
    /* TEST */
    public static int REQUEST_E_MAIL = 3;
    public static Hashtable tableMails = new Hashtable();
    static
    {
        tableMails.put("Vilvens", "claude.vilvens@prov-liege.be");
        tableMails.put("Charlet", "christophe.charlet@prov-liege.be");
        tableMails.put("Madani", "mounawar.madani@prov-liege.be");
        tableMails.put("Wagner", "jean-marc.wagner@prov-liege.be");
    }
    public static Hashtable tablePwdNoms = new Hashtable();
    static
    {
        tablePwdNoms.put("GrosZZ", "Vilvens");
        tablePwdNoms.put("GrosRouteur", "Charlet");
        tablePwdNoms.put("GrosseVoiture", "Madani");
        tablePwdNoms.put("GrosCerveau", "Wagner");
    }
    
    private int Type;
    private String chargeUtile;
    private Socket SocketClient;

    
    public RequeteLUGAP(int Type, String chargeUtile, Socket SocketClient) {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
        this.SocketClient = SocketClient;
    }

    public RequeteLUGAP(int Type, String chargeUtile) {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
    }
       
    
    @Override
    public Runnable createRunnable(final Socket s, final ConsoleServeur cs) {
        /*switch(getType())
        {
            case REQUEST_E_MAIL:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteEMail(s, cs);
                    }            
                };
            case REQUEST_LOGIN_PORTER:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiteRequeteLoginPorter(s, cs);
                    }            
                };
            case REQUEST_TEMPORARY_KEY:
                return new Runnable() 
                {
                    public void run() 
                    {
                        traiterRequeteKey(s, cs);
                    }            
                };
            default : return null;
        }*/
        
        if (getType() == REQUEST_E_MAIL) {
            return new Runnable()
            {
                public void run()
                {
                    traiteRequeteEMail(s, cs);
                }
            };
        }
        else if (getType()==REQUEST_TEMPORARY_KEY) {
            return new Runnable()
            {
                public void run()
                {
                    traiteRequeteKey(s, cs);
                }
            };
        }
        else 
            return null;
}    
    
    private void traiteRequeteEMail(Socket sock, ConsoleServeur cs)
    {
        // Affichage des informations
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        // la charge utile est le nom du client
        String eMail = (String)tableMails.get(getChargeUtile());
        cs.TraceEvenements(adresseDistante+"#Mail de "+getChargeUtile()+"#"+Thread.currentThread().getName());

        if (eMail != null)
            System.out.println("E-Mail trouvé pour " + getChargeUtile());
        else
        {
            System.out.println("E-Mailnon trouvé pour " + getChargeUtile() + " : " + eMail);
            eMail="?@?";
        }
        // Construction d'une réponse
        ReponseLUGAP rep = new ReponseLUGAP(ReponseLUGAP.EMAIL_OK, getChargeUtile() +
        " : " + eMail);
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void traiteRequeteLoginPorter(Socket s, ConsoleServeur cs) {
        String adresseDistante = s.getRemoteSocketAddress().toString();        
        System.out.println("Debut de traiteRequete : adresse distante = " + adresseDistante);
        byte b;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        
        try {
            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
            if (dis == null || dos == null)
                System.err.println("Erreur : dis et/ou dos est/sont null !");
            
            String user = dis.readUTF();
            System.out.println("Utilisateur = " + user);
            long temps = dis.readLong();
            System.out.println("temps = " + temps);
            double alea = dis.readDouble();
            System.out.println("Nombre aléatoire = " + alea);
            int longueur = dis.readInt();
            System.out.println("Longueur = " + longueur);
            byte[] msgD = new byte[longueur];
            dis.readFully(msgD);
            
            // JBDC
            String reponse = ChercheMotdePasse(user, temps, alea, longueur, msgD);
            ReponseLUGAP rep = new ReponseLUGAP(ReponseLUGAP.LOGIN_OK, getChargeUtile() + " : " + user);
            
            ObjectOutputStream oos;
            
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep); 
            oos.flush();
            oos.close();
        } 
        catch (IOException ex) {            
                System.err.println("Erreur de flux !");
        }    
                
    }
    
    private void traiteRequeteKey(Socket s, ConsoleServeur cs) {
        
    }
    
    
    private String ChercheMotdePasse(String user, long temps, double alea, int longueur, byte[] msgD) {        
        Bean_DB_Access_MySQL BD_airport = new Bean_DB_Access_MySQL("localhost", "3306", "Zeydax", "1234", "bd_airport");
        ResultSet RS;
        String reponse = null;
        
        try {
            BD_airport.Connexion();
            RS = BD_airport.Select("SELECT Password FROM bd_airport.Comptes WHERE Login = \"" + user + "\"");
            if (RS != null) {
                MessageDigest md = MessageDigest.getInstance("MD5", "BC");
                RS.next();
                md.update(user.getBytes());
                md.update(RS.getBytes("Password"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(temps);
                bdos.writeDouble(alea);
                md.update(baos.toByteArray());
                byte[] msgDLocal = md.digest();
                
                if (MessageDigest.isEqual(msgD, msgDLocal)) {
                    reponse = new String("OK - vous êtes connecté au serveur !");
                }
                else {
                    reponse = new String("Désolé - votre demande de connexion est refusée !");
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return reponse;
    }
    
    //Getters - Setters
    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getChargeUtile() {
        return chargeUtile;
    }

    public void setChargeUtile(String chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    public Socket getSocketClient() {
        return SocketClient;
    }

    public void setSocketClient(Socket SocketClient) {
        this.SocketClient = SocketClient;
    }
}
