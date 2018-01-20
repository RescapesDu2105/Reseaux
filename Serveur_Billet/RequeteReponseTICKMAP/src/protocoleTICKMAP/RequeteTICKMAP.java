/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocoleTICKMAP;

import cryptographie.CleSecrete;
import cryptographie.CryptageAsymetrique;
import cryptographie.KeyStoreUtils;
import cryptographie.ClientBD;
import cryptographie.CryptageSymetrique;
import cryptographie.HMACUtils;
import database.utilities.Bean_DB_Access;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import static org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt.DECRYPT_MODE;
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
    public final static int REQUEST_REGISTRATION_FLY = 5;
    public final static int REQUEST_PAYMENT_REGISTRATION = 6;
    
    private final static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ServeurKeyStore.jks";
    private final static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private final static String keyStorePsw = "123Soleil";    
    private final static String aliasKeyStrore = "serveurprivatekey";
    private final static String aliasCertifPublicClientKey = "PublicClientKey";
    private final static String NameFileClientSecretKey = "SecretKeyClient.ser";
    private final static String NameFileClientHMAC = "CleSecreteHMACClient.ser";

    
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
                RS = BD_airport.Select("SELECT Password, Nom, Prenom FROM bd_airport.agents WHERE Poste = \"Employe\" AND Login = \"" + user + "\"");
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

            case REQUEST_REGISTRATION_FLY :
                return new Runnable()
                {
                    public void run()
                    {
                        traitRegistrationFly();
                    }
                };
                
            case REQUEST_PAYMENT_REGISTRATION :
                return new Runnable()
                {
                    public void run()
                    {
                        traiterPaymentReg();
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
            case REQUEST_REGISTRATION_FLY : return "REQUEST_REGISTRATION_FLY";
            case REQUEST_PAYMENT_REGISTRATION : return "REQUEST_PAYMENT_REGISTRATION";
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
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException ex)
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

            /**********************************CLE HMAC***********************************/
            CryptageAsymetrique cryptage = new CryptageAsymetrique();
            byte[] cleCrypteeHMAC = (byte[]) getChargeUtile().get("CleHMAC");
            
            byte[] cleDecrypteHMAC = cryptage.Decrypte(clePriveeServeur, cleCrypteeHMAC);
            
            SecretKey cleClientHMACTemp = new SecretKeySpec(cleDecrypteHMAC, 0, cleDecrypteHMAC.length, "DES");
            CleSecrete cleClientHMAC=new CleSecrete(cleClientHMACTemp);
            cleClientHMAC.SaveCle(keyStoreDirPath,NameFileClientHMAC);
            
            /**********************************CLE SECRETE***********************************/
            byte[] cleCrypteeSecrete = (byte[]) getChargeUtile().get("CleSECRETE");
            byte[] cleDecrypteSecrete = cryptage.Decrypte(clePriveeServeur, cleCrypteeSecrete);
            SecretKey cleClientSecreteTemp = new SecretKeySpec(cleDecrypteSecrete, 0, cleDecrypteSecrete.length, "DES");
            CleSecrete cleClientSecete=new CleSecrete(cleClientSecreteTemp);
            cleClientSecete.SaveCle(keyStoreDirPath,NameFileClientSecretKey);

            Reponse = new ReponseTICKMAP(ReponseTICKMAP.SEND_SYMETRICKEY_OK);
            Reponse.getChargeUtile().put("Message", ReponseTICKMAP.SEND_SYMETRICKEY_MESSAGE);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | KeyStoreException | IOException | CertificateException | UnrecoverableKeyException ex)
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
    
    public void traitRegistrationFly()
    {
        Bean_DB_Access BD_airport;
        ResultSet RS;
        CleSecrete cleClient;
        int idVol;
        File f = new File(keyStoreDirPath+NameFileClientSecretKey);
        
        if(f.exists())
        {
            try 
            {
                ObjectInputStream cleFichier = new ObjectInputStream(new FileInputStream(keyStoreDirPath+NameFileClientSecretKey));
                SecretKey keyLoad=(SecretKey) cleFichier.readObject();
                cleFichier.close();
                cleClient=new CleSecrete(keyLoad);
                
                Cipher dechiffrement = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
                dechiffrement.init(DECRYPT_MODE,cleClient.getCle());
                
                /****************************DECRYPTAGE DE L'IDVOL*********************************/
                System.out.println("Decriptage de l'idVol...");
                byte[] idVolCrypte = (byte[]) getChargeUtile().get("IdVol");
                CryptageSymetrique cryptage = new CryptageSymetrique();
                byte[] IdVolDecrypte = cryptage.Decrypte(keyLoad, idVolCrypte);
                String IdVolStr = new String(IdVolDecrypte);
                idVol= Integer.parseInt(IdVolStr);
                System.out.println("idVol Decrypte(string) : "+IdVolStr);
                System.out.println("idVol Decrypte(int) : "+idVol);
                
                /****************************DECRYPTAGE DU CLIENT*********************************/
                System.out.println("Decriptage du client...");
                SealedObject sealed = (SealedObject) getChargeUtile().get("clientBD");
                ClientBD cli = (ClientBD) sealed.getObject(dechiffrement);
                System.out.println("Nom : "+cli.getNom());
                System.out.println("Prenom : "+cli.getPrenom());
                
                /****************************VERIF DANS LA BD*************************************/
                BD_airport = Connexion_DB();
                if (BD_airport != null)
                {
                    RS = BD_airport.Select("SELECT * FROM VOLS WHERE IdVol="+IdVolStr);
                    if (RS != null)
                    {
                       
                       int PlacesRestantes=0; 
                       while(RS.next())
                       {
                           PlacesRestantes=RS.getInt("PlacesRestantes");
                       }
                       if (PlacesRestantes>=cli.getNbAccompagnant()+1)
                       {
                            Reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_REGISTRATION_FLY_OK);
                            Reponse.getChargeUtile().put("Message", ReponseTICKMAP.REQUEST_REGISTRATION_FLY_MESSAGE);
                            
                            /*************************CRYPTAGE DU MONTANT******************************************/
                            int montant = (cli.getNbAccompagnant()+1)*75;
                            byte[] factureCrypte = cryptage.Crypte(keyLoad, (Integer.toString(montant)).getBytes());
                            Reponse.getChargeUtile().put("Facture", (factureCrypte));
                       }
                       else
                       {
                            Reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_REGISTRATION_FLY_KO);
                            Reponse.getChargeUtile().put("Message", ReponseTICKMAP.REQUEST_REGISTRATION_FLY_KO_MESSAGE);                           
                       }
                    }
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SQLException ex)
            {
                Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void traiterPaymentReg()
    {
        SecretKey cleHMAC;
        Bean_DB_Access BD_airport;
        ResultSet RS;
        
        byte[] messageHMACRecu = (byte[]) getChargeUtile().get("messageHmac");
        byte[] cliByte = (byte[]) getChargeUtile().get("ClientBD");
        
        try
        {
            cleHMACClient = new CleSecrete();
            cleHMAC = cleHMACClient.LoadCle(keyStoreDirPath, NameFileClientHMAC);
            HMACUtils hmac = new HMACUtils(cleHMAC);
            byte[] messageHMAC = hmac.DoHmac(cliByte);
            
            if(MessageDigest.isEqual(messageHMACRecu, messageHMAC))
            {
                ClientBD clientBD = ByteToObject(cliByte);
                System.out.println("Message du client Authentifié !!!");
                
                /***************************ENREGISTREMENT DU CLIENT DANS LA BD***************/
                System.out.println("Enregistrement du client dans la BD !!!");
   
                HashMap<String, Object> client = new HashMap<>();
                client.put("Nom", clientBD.getNom());
                client.put("Prenom", clientBD.getPrenom());
                
                if(clientBD.getNom().length()<3)
                {
                    if(clientBD.getPrenom().length()<3)
                        client.put("Login" , clientBD.getPrenom()+clientBD.getNom());
                    else
                        client.put("Login" , clientBD.getPrenom().substring(0, 3)+clientBD.getNom());
                }
                else if(clientBD.getPrenom().length()<3)
                    client.put("Login" , clientBD.getPrenom()+clientBD.getNom().substring(0, 3));
                else
                    client.put("Login" , clientBD.getPrenom().substring(0, 3)+clientBD.getNom().substring(0,3));
                
                client.put("Password", "1234");
                client.put("nbAccompagnant", clientBD.getNbAccompagnant());
                
                BD_airport = Connexion_DB();
                if (BD_airport != null)
                    BD_airport.Insert("Clients", client);
                                
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_PAYMENT_REGISTRATION_OK);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.REQUEST_PAYMENT_REGISTRATION_MESSAGE);
            }
            else
            {
                Reponse = new ReponseTICKMAP(ReponseTICKMAP.INTERNAL_SERVER_ERROR);
                Reponse.getChargeUtile().put("Message", ReponseTICKMAP.INTERNAL_SERVER_ERROR_MESSAGE);                
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException | ClassNotFoundException | InvalidKeyException | SQLException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientBD ByteToObject(byte[] byteArray)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        ClientBD cli = null;
        
        try
        {
            in = new ObjectInputStream(bis);
            cli = (ClientBD) in.readObject(); 
            in.close();
            
            return cli;
        } catch (IOException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(RequeteTICKMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cli;
    }
}
