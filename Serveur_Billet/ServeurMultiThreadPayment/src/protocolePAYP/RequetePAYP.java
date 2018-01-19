/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolePAYP;

import cryptographie.ClientBD;
import cryptographie.CryptageAsymetrique;
import cryptographie.KeyStoreUtils;
import cryptographie.PayementInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import static org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt.DECRYPT_MODE;
import requetepoolthreads.Requete;

/**
 *
 * @author Doublon
 */
public class RequetePAYP implements Requete, Serializable
{
    public final static int REQUEST_SEND_CERTIFICATE = 0;
    public final static int REQUEST_SEND_PAYMENT = 1;
    
    private final static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ServeurPaymentKeyStore.jks";
    private final static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private final static String keyStorePsw = "123Soleil";    
    private final static String aliasKeyStrore = "serverpaymentprivatekey";
    private final static String aliasCertifPublicClientKey = "CertifClient";
    
    private int Type;
    private HashMap<String, Object> chargeUtile;
    private Socket SocketClient;
    
    private ReponsePAYP Reponse = null;
    private Properties Prop = null;
    
    public RequetePAYP(int Type, HashMap chargeUtile) 
    {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
    }
    
    public RequetePAYP(int Type) 
    {
        this.Type = Type;
        this.chargeUtile = new HashMap<>();
    }
    
    @Override
    public ReponsePAYP getReponse() 
    {
        return Reponse;
    }

    public void setReponse(ReponsePAYP Reponse) 
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
            case REQUEST_SEND_CERTIFICATE:
            return new Runnable() 
            {
                public void run() 
                {
                    traiterSendCertif();
                }            
            };
            
            case REQUEST_SEND_PAYMENT:
            return new Runnable() 
            {
                public void run() 
                {
                    traiterSendPayment();
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
            case REQUEST_SEND_CERTIFICATE: return "REQUEST_SEND_CERTIFICATE";
            case REQUEST_SEND_PAYMENT: return "REQUEST_SEND_PAYMENT";
            default : return null;
        }
    }
    
    public void traiterSendCertif()
    {
        KeyStoreUtils ks = null;
        X509Certificate certifClient=(X509Certificate) getChargeUtile().get("Certificate");
        
        try
        {
            ks=new KeyStoreUtils(keyStorePath,keyStorePsw,aliasKeyStrore);
            
            ks.saveCertificate(aliasCertifPublicClientKey, certifClient);
            ks.SaveKeyStore(keyStorePath, keyStorePsw);
            
        } catch (KeyStoreException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Reponse = new ReponsePAYP(ReponsePAYP.REQUEST_SEND_CERTIFICATE_OK);
        Reponse.getChargeUtile().put("Message", ReponsePAYP.REQUEST_SEND_CERTIFICATE_MESSAGE);
        Reponse.getChargeUtile().put("Certificate",ks.getCertif());        
    }
    
    public void traiterSendPayment()
    {
        KeyStoreUtils ks = null;
        X509Certificate certifClient = null;
        byte[] signature = (byte[]) getChargeUtile().get("Signature");
        
        try
        {
            ks=new KeyStoreUtils(keyStorePath,keyStorePsw,aliasKeyStrore);
            certifClient = ks.loadCertificate(aliasCertifPublicClientKey);
            CryptageAsymetrique cryptage = new CryptageAsymetrique();
            /********************************DECRYPTAGE DU PAYEMENT*******************************/
            Cipher dechiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            dechiffrement.init(DECRYPT_MODE,ks.getClePrivee());
            SealedObject sealed = (SealedObject) getChargeUtile().get("PayementCrypte");
            PayementInfo pay = (PayementInfo) sealed.getObject(dechiffrement);
            System.out.println("Nom : "+pay.getNom());
            System.out.println("Carde : "+pay.getCreditCard());
            System.out.println("Montant : "+pay.getMontant());
            
            /********************************VERIFICATION DE LA SIGNATURE*************************/
            byte[] sealedByte = ObcjetToByte(sealed);
            boolean ok = cryptage.VerifSignature(certifClient.getPublicKey(), sealedByte, signature);
            
        } catch (KeyStoreException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
        Reponse = new ReponsePAYP(ReponsePAYP.REQUEST_SEND_PAYMENT_OK);
        Reponse.getChargeUtile().put("Message", ReponsePAYP.REQUEST_SEND_PAYMENT_MESSAGE);
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
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cli;
    }
    
    public byte[] ObcjetToByte(Object o)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] byteArray = null;
        
        try
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.flush();
            byteArray = bos.toByteArray();
            
            bos.close();
            return byteArray;
        } catch (IOException ex)
        {
            Logger.getLogger(RequetePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArray;
    }  
}
