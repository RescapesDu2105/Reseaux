/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Doublon
 */
public class Certificats
{
    private InputStream inStream;
    private CertificateFactory cf;
    private X509Certificate cert;
    private PublicKey clepublique;

    public Certificats(String path)
    {
        try
        {
            inStream = new FileInputStream(path);
            cf=CertificateFactory.getInstance("X.509");
            cert=(X509Certificate)cf.generateCertificate(inStream);
            System.out.println("Classe instanciée : " + cert.getClass().getName());
            System.out.println("Type de certificat : " + cert.getType());
            System.out.println("Nom du propriétaire du certificat : " +cert.getSubjectDN().getName());
            
            setClepublique(cert.getPublicKey());
            System.out.println("... sa clé publique : " + getClepublique().toString());
            System.out.println("... la classe instanciée par celle-ci : " +getClepublique().getClass().getName());
            System.out.println("Dates limites de validité : [" + cert.getNotBefore() + " - " +cert.getNotAfter() + "]"); 

            cert.checkValidity(); 

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Certificats.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(Certificats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public InputStream getInStream()
    {
        return inStream;
    }

    public void setInStream(InputStream inStream)
    {
        this.inStream = inStream;
    }

    public CertificateFactory getCf()
    {
        return cf;
    }

    public void setCf(CertificateFactory cf)
    {
        this.cf = cf;
    }

    public X509Certificate getCerf()
    {
        return cert;
    }

    public void setCerf(X509Certificate cerf)
    {
        this.cert = cerf;
    }

    public X509Certificate getCert()
    {
        return cert;
    }

    public void setCert(X509Certificate cert)
    {
        this.cert = cert;
    }

    public PublicKey getClepublique()
    {
        return clepublique;
    }

    public void setClepublique(PublicKey clepublique)
    {
        this.clepublique = clepublique;
    }
   
}
