/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.*;
import java.security.*;
import java.security.cert.*; 
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Doublon
 */
public class KeyStoreUtils
{
    private static String codeProvider = "BC"; 
    private static final String algoSIGN = "SHA1withRSA";

    private KeyStore ks;
    private PrivateKey clePrivee;
    private PublicKey clePublique;
    private Signature signature;
    private X509Certificate certif;
    
    public KeyStoreUtils(String pathKeyStore , String pswd , String alias) 
            throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchProviderException
    {
        System.out.println("Coucou"); 
        Security.addProvider(new BouncyCastleProvider());
        ks=KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(pathKeyStore),pswd.toCharArray());
        System.out.println("Recuperation de la cle privee"); 
        setClePrivee((PrivateKey) ks.getKey(alias, pswd.toCharArray()));
        System.out.println(" *** Cle privee recuperee = " + clePrivee.toString());
        
        System.out.println("Recuperation du certificat"); 
        signature = Signature.getInstance(algoSIGN,codeProvider);
        setCertif((X509Certificate)ks.getCertificate(alias));
        
        System.out.println("Recuperation de la cle publique");
        setClePublique(certif.getPublicKey());
        System.out.println("*** Cle publique recuperee = "+getClePublique().toString());
        
        /*boolean ok = signature.verify(signature);
        if (ok) System.out.println("Signature testee avec succes");
        else System.out.println("Signature testee sans succes"); */
    }
    
    public void saveCertificate(String alias , X509Certificate certifRecu) throws KeyStoreException
    {
        ks.setCertificateEntry(alias, certifRecu);
    }
    
    public KeyStore getKs()
    {
        return ks;
    }

    public void setKs(KeyStore ks)
    {
        this.ks = ks;
    }

    public PrivateKey getClePrivee()
    {
        return clePrivee;
    }

    public void setClePrivee(PrivateKey clePrivee)
    {
        this.clePrivee = clePrivee;
    }

    public PublicKey getClePublique()
    {
        return clePublique;
    }

    public void setClePublique(PublicKey clePublique)
    {
        this.clePublique = clePublique;
    }

    public Signature getSignature()
    {
        return signature;
    }

    public void setSignature(Signature signature)
    {
        this.signature = signature;
    }

    public X509Certificate getCertif()
    {
        return certif;
    }

    public void setCertif(X509Certificate certif)
    {
        this.certif = certif;
    }
    
    
    
}
