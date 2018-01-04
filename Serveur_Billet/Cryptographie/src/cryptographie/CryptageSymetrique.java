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
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Doublon
 */
public class CryptageSymetrique
{
    private static String codeProvider ="BC";
    private static String algoCrypt="DES/ECB/PKCS5Padding";
    private Cipher chiffrement;

    public CryptageSymetrique() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        Security.addProvider(new BouncyCastleProvider());
        setChiffrement(Cipher.getInstance(algoCrypt,codeProvider));
    }

    public byte[] Crypte(SecretKey cle,byte[] objetACrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        getChiffrement().init(Cipher.ENCRYPT_MODE, cle);
        byte[] objetCrypte = getChiffrement().doFinal(objetACrypte);
        System.out.println("Cryptage de : ");
        System.out.println(new String(objetACrypte.toString().getBytes()) + " ---> " + objetCrypte);
        return objetCrypte;  
    }
    
    public byte[] Decrypte(SecretKey cle,byte[] objetADecrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        getChiffrement().init(Cipher.DECRYPT_MODE, cle);
        byte[] objetDecrypte = getChiffrement().doFinal(objetADecrypte);
        System.out.println("Cryptage de : ");
        System.out.println(new String(objetADecrypte.toString().getBytes()) + " ---> " + objetDecrypte);
        return objetDecrypte;  
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
