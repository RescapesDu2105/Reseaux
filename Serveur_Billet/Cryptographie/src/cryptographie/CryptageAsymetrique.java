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
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Doublon
 */
public class CryptageAsymetrique
{
    private static String codeProvider ="BC";
    private static String algoCrypt="RSA/ECB/PKCS1Padding";
    private Cipher chiffrement;

    public CryptageAsymetrique() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        Security.addProvider(new BouncyCastleProvider());
        setChiffrement(Cipher.getInstance(algoCrypt,codeProvider));
    }

    public byte[] Crypte(PublicKey cle,Object objetACrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        getChiffrement().init(Cipher.ENCRYPT_MODE, cle);
        byte[] objetCrypte = getChiffrement().doFinal (objetACrypte.toString().getBytes());
        System.out.println("Cryptage de : ");
        System.out.println(new String(objetACrypte.toString().getBytes()) + " ---> " + objetCrypte);
        return objetCrypte;
    }
    
    public byte[] Decrypte(PrivateKey cle , Object objetADecrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        System.out.println("ici");
        System.out.println("objetADecrypte : "+objetADecrypte);
        getChiffrement().init(Cipher.DECRYPT_MODE, cle);
        byte[] objetDecrypte = getChiffrement().doFinal(objetADecrypte.toString().getBytes());
        System.out.println("Decryptage de : ");
        System.out.println(objetADecrypte.toString().getBytes() + " ---> " +new String(objetDecrypte).substring(0,objetDecrypte.length));
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
