/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolePAYP;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import requetepoolthreads.Requete;

/**
 *
 * @author Doublon
 */
public class RequetePAYP implements Requete, Serializable
{
    public final static int REQUEST_SEND_CERTIFICATE = 0;
    
    private static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ServeurPaymentKeyStore.jks";
    private static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private static String keyStorePsw = "123Soleil";    
    private static String aliasKeyStrore = "serveurprivatekey";
    private static String aliasCertifPublicClientKey = "PublicClientKey";
    
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
            /*case REQUEST_LOG_OUT_PORTER: return "REQUEST_LOG_OUT_PORTER";
            case REQUEST_LOGIN_PORTER: return "REQUEST_LOGIN_PORTER"; */
            case REQUEST_SEND_CERTIFICATE: return "REQUEST_SEND_CERTIFICATE";
            default : return null;
        }
    }
    
    public void traiterSendCertif()
    {
        System.out.println("Coucou");
        Reponse = new ReponsePAYP(ReponsePAYP.REQUEST_SEND_CERTIFICATE_OK);
        Reponse.getChargeUtile().put("Message", ReponsePAYP.REQUEST_SEND_CERTIFICATE_MESSAGE);   
    }
}
