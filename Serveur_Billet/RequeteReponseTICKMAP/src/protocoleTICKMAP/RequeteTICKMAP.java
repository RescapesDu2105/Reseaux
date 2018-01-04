/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocoleTICKMAP;

import cryptographie.CleSecrete;
import cryptographie.CryptageAsymetrique;
import cryptographie.KeyStoreUtils;
import database.utilities.Bean_DB_Access;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.Requete;

/**
 *
 * @author Doublon
 */
public class RequeteTICKMAP implements Requete, Serializable
{
    
    public final static int REQUEST_LOG_OUT_PORTER = 0;
    public final static int REQUEST_LOGIN_PORTER = 1;
    public final static int REQUEST_SEND_CERTIFICATE = 2;
    public final static int REQUEST_SEND_SYMETRIC_KEY = 3;
    public final static int REQUEST_SEND_LIST_OF_FLY = 4;
    
    private static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ServeurKeyStore.jks";
    private static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private static String keyStorePsw = "123Soleil";    
    private static String aliasKeyStrore = "serveurprivatekey";
    private static String aliasCertifPublicClientKey = "PublicClientKey";
    private static String NameFileClientSecretKey = "SecretClientKey.ser";
    private static String NameFileClientHMAC = "ClientHMAC.ser";

    
    private int Type;
    private HashMap<String, Object> chargeUtile;
    private Socket SocketClient;
    
    private ReponseTICKMAP Reponse = null;
    private Properties Prop = null;
    
    private X509Certificate certifClient;
    private KeyStoreUtils ks;
    private CleSecrete cleHMACClient ;
    private PrivateKey clePriveeServeur;

    public RequeteTICKMAP(int Type, HashMap chargeUtile) 
    {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
    }
    
    public RequeteTICKMAP(int Type) 
    {
        this.Type = Type;
        this.chargeUtile = new HashMap<>();
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
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + ex.getMessage());
            }
        }
        
        BD_airport.Deconnexion();
                
        return Champs;
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
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + Error);
            }                
        }        
        
        return BD_airport;
    }
    
    @Override
    public ReponseTICKMAP getReponse() 
    {
        return Reponse;
    }

    public void setReponse(ReponseTICKMAP Reponse) 
    {
        this.Reponse = Reponse;
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
    public HashMap getChargeUtile() 
    {
        return chargeUtile;
    }

    public void setChargeUtile(HashMap chargeUtile) 
    {
        this.chargeUtile = chargeUtile;
    }

    public Socket getSocketClient() 
    {
        return SocketClient;
    }

    public void setSocketClient(Socket SocketClient) 
    {
        this.SocketClient = SocketClient;
    }

    public Properties getProp() 
    {
        return Prop;
    }

    public void setProp(Properties Prop) 
    {
        this.Prop = Prop;
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
                
            case REQUEST_SEND_CERTIFICATE :
                return new Runnable()
                {
                    public void run()
                    {
                        traiterSendCertificate();
                    }
                };
                
            case REQUEST_SEND_SYMETRIC_KEY :
                return new Runnable()
                {
                    public void run()
                    {
                        traiterSendSymetricKey();
                    }
                };
                
            case REQUEST_SEND_LIST_OF_FLY :
                return new Runnable()
                {
                    public void run()
                    {
                        traiterSendListOfFly();
                    }
                };                
            default : return null;
        }
    }

    @Override
    public String getNomTypeRequete() 
    {
        switch(getType()) 
        {
            case REQUEST_LOG_OUT_PORTER: return "REQUEST_LOG_OUT_PORTER";
            case REQUEST_LOGIN_PORTER: return "REQUEST_LOGIN_PORTER"; 
            case REQUEST_SEND_CERTIFICATE : return "REQUEST_SEND_CERTIFICATE";
            case REQUEST_SEND_SYMETRIC_KEY : return "REQUEST_SEND_SYMETRIC_KEY";
            case REQUEST_SEND_LIST_OF_FLY : return "REQUEST_SEND_LIST_OF_FLY";
            default : return null;
        }
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
                    Reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_OK);
                    Reponse.getChargeUtile().put("Message", ReponseTICKMAP.LOGIN_OK_MESSAGE);
                    Reponse.getChargeUtile().put("Nom", Champs[1]);
                    Reponse.getChargeUtile().put("Prenom", Champs[2]);
                }
                else 
                {
                    Reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_KO);
                    Reponse.getChargeUtile().put("Message", ReponseTICKMAP.WRONG_USER_PASSWORD_MESSAGE);                        
                    System.out.println(ReponseTICKMAP.WRONG_USER_PASSWORD_MESSAGE);
                }
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) 
            {
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);
            }; 
        }       
        else
        {
            Reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_KO);
            Reponse.getChargeUtile().put("Message", ReponseTICKMAP.WRONG_USER_PASSWORD_MESSAGE);                        
            System.out.println(ReponseTICKMAP.WRONG_USER_PASSWORD_MESSAGE);
        }               
    }
    
    private void traiteRequeteLogOutPorter() 
    {
        Reponse = new ReponseTICKMAP(ReponseTICKMAP.LOG_OUT_OK);
        Reponse.getChargeUtile().put("Message", ReponseTICKMAP.LOG_OUT_OK_MESSAGE);
    }
    
    private void traiterSendCertificate()
    {
        System.out.println(keyStorePath);
        certifClient=(X509Certificate) getChargeUtile().get("Certificate");
        System.out.println("");
        System.out.println("Reception du certificat du client");
        System.out.println("Classe instanciée : " + certifClient.getClass().getName());
        System.out.println("Type de certificat : " + certifClient.getType());
        System.out.println("Nom du propriétaire du certificat : " +certifClient.getSubjectDN().getName());
        System.out.println("Dates limites de validité : [" + certifClient.getNotBefore() + " - " +certifClient.getNotAfter() + "]");   
        
        
        System.out.println("... sa clé publique : " + certifClient.getPublicKey().toString());
        System.out.println("... la classe instanciée par celle-ci : " +certifClient.getPublicKey().getClass().getName());
        
       
        try
        {
            ks=new KeyStoreUtils(keyStorePath,keyStorePsw,aliasKeyStrore);
            clePriveeServeur= ks.getClePrivee();
            System.out.println("");
            System.out.println("Cle privee du Keystore : "+ks.getClePrivee().toString());
            System.out.println("Cle privee du Keystore : "+clePriveeServeur.toString());
            
            ks.saveCertificate(aliasCertifPublicClientKey, certifClient);
            ks.SaveKeyStore(keyStorePath, keyStorePsw);
        } catch (KeyStoreException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Reponse = new ReponseTICKMAP(ReponseTICKMAP.SEND_CERTIFICATE_OK);
        Reponse.getChargeUtile().put("Message", ReponseTICKMAP.SEND_CERTIFICATE_OK_MESSAGE);
        Reponse.getChargeUtile().put("Certificate",ks.getCertif());
    }
    
    public void traiterSendSymetricKey()
    {
        try
        {
            ks=new KeyStoreUtils(keyStorePath,keyStorePsw,aliasKeyStrore);
            clePriveeServeur= ks.getClePrivee();
            /*System.out.println("");
            System.out.println("Cle privee du Keystore : "+clePriveeServeur.toString());*/
            /**********************************CLE HMAC***********************************/
            CryptageAsymetrique cryptage = new CryptageAsymetrique();
            byte[] cleCrypteeHMAC = (byte[]) getChargeUtile().get("CleHMAC");
            //System.out.println("ChargeUtile : "+ cleCryptee);
            
            byte[] cleDecrypteHMAC = cryptage.Decrypte(clePriveeServeur, cleCrypteeHMAC);
            /*System.out.println("");
            System.out.println("Cle Decrypte : "+cleDecrypte);*/
            
            SecretKey cleClientHMACTemp = new SecretKeySpec(cleDecrypteHMAC, 0, cleDecrypteHMAC.length, "DES");
            CleSecrete cleClientHMAC=new CleSecrete(cleClientHMACTemp);
            cleClientHMAC.SaveCle(keyStoreDirPath,NameFileClientHMAC);
            
            /**********************************CLE SECRETE***********************************/
            byte[] cleCrypteeSecrete = (byte[]) getChargeUtile().get("CleSECRETE");
            byte[] cleDecrypteSecrete = cryptage.Decrypte(clePriveeServeur, cleCrypteeSecrete);
            SecretKey cleClientSecreteTemp = new SecretKeySpec(cleDecrypteSecrete, 0, cleDecrypteSecrete.length, "DES");
            CleSecrete cleClientSecete=new CleSecrete(cleClientSecreteTemp);
            cleClientHMAC.SaveCle(keyStoreDirPath,NameFileClientSecretKey);
            /*System.out.println("");
            System.out.println("Cle decripté : " +cleClientHMAC.toString());*/
            Reponse = new ReponseTICKMAP(ReponseTICKMAP.SEND_SYMETRICKEY_OK);
            Reponse.getChargeUtile().put("Message", ReponseTICKMAP.SEND_SYMETRICKEY_MESSAGE);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void traiterSendListOfFly()
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        String[] Champs = null;
        
        BD_airport = Connexion_DB();
        
        if (BD_airport != null)
        {
            int i=1;
            try 
            {                        
                RS = BD_airport.Select("SELECT * FROM VOLS");
                //RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.agents WHERE Poste = \"Bagagiste\" AND Login = \"" + user + "\"");
                if (RS != null) 
                {         
                    Reponse = new ReponseTICKMAP(ReponseTICKMAP.LIST_OF_FLY_OK);
                    while(RS.next())
                    {
                        int IdVol = RS.getInt("IdVol");
                        int NumeroVol = RS.getInt("NumeroVol");
                        String Destination = RS.getString("Destination");
                        Timestamp DateHeureDepart = RS.getTimestamp("HeureDepart");
                        int PlacesRestantes = RS.getInt("PlacesRestantes");

                        HashMap<String, Object> hm = new HashMap<>();
                        
                        hm.put("IdVol", IdVol);
                        hm.put("NumeroVol", NumeroVol);
                        hm.put("Destination", Destination);
                        hm.put("DateHeureDepart", DateHeureDepart);
                        hm.put("PlacesRestantes",PlacesRestantes);

                        Reponse.getChargeUtile().put(Integer.toString(i), hm);
                        i++;
                    }
                    Reponse.getChargeUtile().put("Message", ReponseTICKMAP.LIST_OF_FLY_MESSAGE);
                }
            }       
            catch (SQLException ex) 
            {            
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);
                System.out.println(ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE + " : " + ex.getMessage());
            }
        }
        
        BD_airport.Deconnexion();
        //return Champs;        
    }
}
