/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Doublon
 */
public class CryptoUtils
{
    private static final String algoASM = "RSA/ECB/PKCS1Padding";
    private static final String algoSYM = "DES/ECB/PKCS5Padding";
    private static final String algoSIGN = "SHA1withRSA";
    private static final String algoHMAC = "HMAC-MD5";
    private static final String algoDigest = "SHA-1";
    private static final String codeProvider = "BC";

    private Signature signature;
    private Cipher chiffrementASM;
    private Cipher chiffrementSYM;
    private Mac hmac;
    private MessageDigest md;
   
    //private SecretKey cleSecrete;
    
    public CryptoUtils() 
            throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException, 
            KeyStoreException, CertificateException, UnrecoverableKeyException 
    {
        Security.addProvider(new BouncyCastleProvider());
        signature = Signature.getInstance(algoSIGN, codeProvider);
        chiffrementASM = Cipher.getInstance(algoASM, codeProvider);
        chiffrementSYM = Cipher.getInstance(algoSYM, codeProvider);
        hmac = Mac.getInstance(algoHMAC, codeProvider);
        md = MessageDigest.getInstance(algoDigest, codeProvider);
    }

    public void generateSeKey(String fileLocation) throws IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        /* Génération de la clé */
        KeyGenerator keyGen = KeyGenerator.getInstance("DES", codeProvider);
        keyGen.init(new SecureRandom());
        SecretKey key = keyGen.generateKey();
        
        /* Ecriture de la clé */
        ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileLocation));
        keyFile.writeObject(key);
        keyFile.close();
    }
    
    public SecretKey getSecretKey(String fileLocation) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(fileLocation));
        SecretKey secretKey = (SecretKey) keyFile.readObject();
        keyFile.close();
        
        return secretKey;        
    }
    
    public PublicKey getPublicKeyFromCertificate(String certificateLocation) throws FileNotFoundException, CertificateException
    {
        FileInputStream fin = new FileInputStream(certificateLocation);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        return certificate.getPublicKey();
    }
    
    public PublicKey getPublicKeyFromCertificate(String JKSLocation, String certificatName, String password) 
                throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException 
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
        
        X509Certificate certificat = (X509Certificate) ks.getCertificate(certificatName);
        
        return (PublicKey) certificat.getPublicKey();
    }
    
    public X509Certificate getCertificate(String certificateLocation) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException
    {
        FileInputStream fin = new FileInputStream(certificateLocation);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        return certificate;
    }
    
    public X509Certificate getCertificate(String JKSLocation, String certificateName, String password) 
            throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
        
        return (X509Certificate) ks.getCertificate(certificateName);
    }
    
        public PrivateKey getPrivateKeyFromCertificate(String JKSLocation, String certificatName, String password, String passwordKey) 
                throws FileNotFoundException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException  
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
   
        return (PrivateKey) ks.getKey(certificatName, passwordKey.toCharArray());
    }
        
    public byte[] encryptASM(byte[] message, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        chiffrementASM.init(Cipher.ENCRYPT_MODE, publicKey);
        return chiffrementASM.doFinal(message);
    }
    
    public byte[] decryptASM(byte[] message, PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        chiffrementASM.init(Cipher.DECRYPT_MODE, privateKey);
        return chiffrementASM.doFinal(message);
    }
    
    public byte[] encryptSYM(byte[] message, SecretKey secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
    {
        chiffrementSYM.init(Cipher.ENCRYPT_MODE, secretKey);
        return chiffrementSYM.doFinal(message);
    }
    
    public byte[] decryptSYM(byte[] cypherText, SecretKey secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        chiffrementSYM.init(Cipher.DECRYPT_MODE, secretKey);
        return chiffrementSYM.doFinal(cypherText);
        
    }
    
    public byte[] hashHMAC(byte[] message, SecretKey secretKey) throws InvalidKeyException
    {
        hmac.init(secretKey);
        hmac.update(message);
        return hmac.doFinal();
    }
    
    public boolean verifyHMAC(byte[] receivedHashed, byte[] message, SecretKey secretKey) throws InvalidKeyException
    {
        hmac.init(secretKey);
        hmac.update(message);
        byte[] generatedHashed = hmac.doFinal();
        
        if(MessageDigest.isEqual(generatedHashed, receivedHashed))
            return true;
        else
            return false;
    }
    
    public byte[] makeDigest(String message, long temps, double alea) throws IOException
    {
        md.update(message.getBytes());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(temps);
        bdos.writeDouble(alea);
        
        md.update(baos.toByteArray());
        
        return md.digest();
    }
    
    public byte[] getByteFromObject(Object o) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }
    
    public Object getObjectFromByte(byte[] b) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
    
    public byte[] getSignature(byte[] message, PrivateKey privateKey) throws SignatureException, InvalidKeyException
    {
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }
    
    public boolean verifySignature(byte[] message, byte[] sign, PublicKey publicKey) throws InvalidKeyException, SignatureException
    {
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(sign);
    }
    
    public Signature getSignature()
    {
        return signature;
    }

    public void setSignature(Signature signature)
    {
        this.signature = signature;
    }

    public Cipher getChiffrementASM()
    {
        return chiffrementASM;
    }

    public void setChiffrementASM(Cipher chiffrementASM)
    {
        this.chiffrementASM = chiffrementASM;
    }

    public Cipher getChiffrementSYM()
    {
        return chiffrementSYM;
    }

    public void setChiffrementSYM(Cipher chiffrementSYM)
    {
        this.chiffrementSYM = chiffrementSYM;
    }

    public Mac getHmac()
    {
        return hmac;
    }

    public void setHmac(Mac hmac)
    {
        this.hmac = hmac;
    }

    public MessageDigest getMd()
    {
        return md;
    }

    public void setMd(MessageDigest md)
    {
        this.md = md;
    }
        
        
}
