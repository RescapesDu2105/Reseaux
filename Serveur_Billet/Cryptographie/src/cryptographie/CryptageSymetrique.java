/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Doublon
 */
public class CryptageSymetrique
{
    private static String codeProvider ="BC";
    private KeyGenerator cleGen ;
    private SecretKey cle;
    private Cipher chiffrement;

    public CryptageSymetrique()
    {
        try
        {
            cleGen= KeyGenerator.getInstance("DES", codeProvider);
            cle=cleGen.generateKey();
            chiffrement=Cipher.getInstance("DES/ECB/PKCS5Padding",codeProvider);
            chiffrement.init(Cipher.ENCRYPT_MODE,cle); 
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(CryptageSymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(CryptageSymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(CryptageSymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(CryptageSymetrique.class.getName()).log(Level.SEVERE, null, ex);
        }
        cleGen.init(new SecureRandom()); 
    }
    
    public KeyGenerator getCleGen()
    {
        return cleGen;
    }

    public void setCleGen(KeyGenerator cleGen)
    {
        this.cleGen = cleGen;
    }

    public SecretKey getCle()
    {
        return cle;
    }

    public void setCle(SecretKey cle)
    {
        this.cle = cle;
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
