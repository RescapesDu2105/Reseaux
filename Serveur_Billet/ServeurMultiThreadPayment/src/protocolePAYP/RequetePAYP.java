/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolePAYP;

import cryptographie.KeyStoreUtils;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.Requete;

/**
 *
 * @author Doublon
 */
public class RequetePAYP implements Requete, Serializable
{
    public final static int REQUEST_SEND_CERTIFICATE = 0;
    
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
            
            default : return null;
        }
    }
    
    @Override
    public String getNomTypeRequete() 
    {
        switch(getType()) 
        {
            case REQUEST_SEND_CERTIFICATE: return "REQUEST_SEND_CERTIFICATE";
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
}
