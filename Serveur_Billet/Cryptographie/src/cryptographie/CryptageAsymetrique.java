/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Doublon
 */
public class CryptageAsymetrique
{
    private static String codeProvider = "BC"; 
    private ClesPourCryptageAsymetrique cles;
    private PublicKey clePublique;
    private PrivateKey clePrivee;
    private Cipher chiffrement;

    public CryptageAsymetrique()
    {
        try
        {
            cles=new ClesPourCryptageAsymetrique();
            clePublique=cles.getClepublique();
            clePrivee=cles.getCleprivee();
            chiffrement=Cipher.getInstance("RSA/ECB/PKCS#1",codeProvider);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CryptageAsymetrique(PublicKey clePubliqueRecue , PrivateKey clePriveeRecue)
    {
        try
        {
            setClePublique(clePubliqueRecue);
            setClePrivee(clePriveeRecue);
            chiffrement=Cipher.getInstance("RSA/ECB/PKCS#1",codeProvider);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Crypte()
    {
        try
        {
            getChiffrement().init(Cipher.ENCRYPT_MODE, getClePublique());
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(CryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Decrypte() throws InvalidKeyException
    {
        getChiffrement().init(Cipher.DECRYPT_MODE, getClePublique());
    }
    
    public ClesPourCryptageAsymetrique getCles()
    {
        return cles;
    }

    public void setCles(ClesPourCryptageAsymetrique cles)
    {
        this.cles = cles;
    }

    public PublicKey getClePublique()
    {
        return clePublique;
    }

    public void setClePublique(PublicKey clePublique)
    {
        this.clePublique = clePublique;
    }

    public PrivateKey getClePrivee()
    {
        return clePrivee;
    }

    public void setClePrivee(PrivateKey clePrivee)
    {
        this.clePrivee = clePrivee;
    }

    public Cipher getChiffrement()
    {
        return chiffrement;
    }

    public void setChiffrement(Cipher chiffrement)
    {
        this.chiffrement = chiffrement;
    }
    
    
    
}
