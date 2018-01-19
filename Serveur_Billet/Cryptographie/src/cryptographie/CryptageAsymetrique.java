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
import java.security.Signature;
import java.security.SignatureException;
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
    private final static String codeProvider = "BC";
    private final static String algoCrypt = "RSA/ECB/PKCS1Padding";
    private final static String algoSignature = "SHA1withRSA";
    private Cipher chiffrement;
    private Signature signature;

    public CryptageAsymetrique() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        Security.addProvider(new BouncyCastleProvider());
        setChiffrement(Cipher.getInstance(algoCrypt,codeProvider));
        setSignature(Signature.getInstance(algoSignature,codeProvider));
    }

    public byte[] Crypte(PublicKey cle,byte[] objetACrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        System.out.println("objetACrypte (en bytes): "+objetACrypte);
        getChiffrement().init(Cipher.ENCRYPT_MODE, cle);
        byte[] objetCrypte = getChiffrement().doFinal(objetACrypte);
        System.out.println("Cryptage de : ");
        System.out.println(new String(objetACrypte.toString().getBytes()) + " ---> " + objetCrypte);
        return objetCrypte;
    }
    
    public byte[] Decrypte(PrivateKey cle , byte[] objetADecrypte) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        System.out.println("objetADecrypte (en bytes): "+objetADecrypte);
        getChiffrement().init(Cipher.DECRYPT_MODE, cle);
        byte[] objetDecrypte = getChiffrement().doFinal(objetADecrypte);
        System.out.println("Decryptage de : ");
        System.out.println(new String(objetADecrypte.toString().getBytes()) + " ---> " + objetDecrypte);
        return objetDecrypte;
    }
    
    public byte[] Signe(PrivateKey cle , byte[] messageASigne) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
    {
        getSignature().initSign(cle);
        getSignature().update(messageASigne);
        return getSignature().sign();
    }
    
    public boolean VerifSignature(PublicKey cle , byte[] message , byte[] messaSigne) throws InvalidKeyException, SignatureException
    {
        getSignature().initVerify(cle);
        getSignature().update(message);
        return getSignature().verify(messaSigne);
    }
    
    public Cipher getChiffrement()
    {
        return chiffrement;
    }

    public void setChiffrement(Cipher chiffrement)
    {
        this.chiffrement = chiffrement;
    }

    public Signature getSignature()
    {
        return signature;
    }

    public void setSignature(Signature signature)
    {
        this.signature = signature;
    }
    
    
    
}
