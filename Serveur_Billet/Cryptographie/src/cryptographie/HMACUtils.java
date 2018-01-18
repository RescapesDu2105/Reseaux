/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;


/**
 *
 * @author Doublon
 */
public class HMACUtils
{
    private static String codeProvider  = "BC";
    private static String algoHMAC = "HMAC-MD5";    
    
    private SecretKey cleHMAC;
    private Mac hmac ;
    
    public HMACUtils(SecretKey cle) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        setCleHMAC(cle);
        setHmac(Mac.getInstance(algoHMAC,codeProvider));
    }
    
    public byte[] DoHmac(byte[] message) throws InvalidKeyException
    {
        hmac.init(getCleHMAC());
        hmac.update(message);
        return hmac.doFinal();
    }

    public SecretKey getCleHMAC()
    {
        return cleHMAC;
    }

    public void setCleHMAC(SecretKey cleHMAC)
    {
        this.cleHMAC = cleHMAC;
    }

    public Mac getHmac()
    {
        return hmac;
    }

    public void setHmac(Mac hmac)
    {
        this.hmac = hmac;
    }
    
    
}
