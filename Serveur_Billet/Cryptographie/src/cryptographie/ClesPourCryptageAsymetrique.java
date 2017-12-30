/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Doublon
 */
public class ClesPourCryptageAsymetrique
{
    private static String codeProvider = "BC";  
    private static int se=1024;
    private KeyPairGenerator genCles;
    private KeyPair deuxCles;
    private PublicKey clepublique;
    private PrivateKey cleprivee;

    public ClesPourCryptageAsymetrique()
    {
        try
        {
            genCles=KeyPairGenerator.getInstance("RSA","BC");
            genCles.initialize(se, new SecureRandom());
            deuxCles = genCles.generateKeyPair(); 
            clepublique=deuxCles.getPublic();
            cleprivee = deuxCles.getPrivate(); 
            
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(ClesPourCryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(ClesPourCryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SerialiserCle()
    {
        ObjectOutputStream clePubliqueFich = null,clePriveeFich = null;
        String path = "D:\\GitHub\\Reseaux\\Serveur_Billet\\cle"; 
        try
        {
            /*****************CLE PUBLIQUE*************************************/
            clePubliqueFich = new ObjectOutputStream(new FileOutputStream(path+"\\xp.ser"));
            System.out.println("fichier ouvert");
            clePubliqueFich.writeObject(getClepublique());
            System.out.println("cle ecrite");
            clePubliqueFich.close(); System.out.println("fichier ferme"); 
            
            /*****************CLE PRIVEE*************************************/
            clePriveeFich = new ObjectOutputStream(new FileOutputStream(path+"\\xs.ser"));
            clePriveeFich.writeObject(getCleprivee());
            clePriveeFich.close(); 
            
            System.out.println(" *** Cle publique generee = " + getClepublique());
            System.out.println(" *** Cle privee generee = " + getCleprivee()); 
        } catch (IOException ex)
        {
            Logger.getLogger(ClesPourCryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                clePubliqueFich.close();
            } catch (IOException ex)
            {
                Logger.getLogger(ClesPourCryptageAsymetrique.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public KeyPairGenerator getGenCles()
    {
        return genCles;
    }

    public void setGenCles(KeyPairGenerator genCles)
    {
        this.genCles = genCles;
    }

    public KeyPair getDeuxCles()
    {
        return deuxCles;
    }

    public void setDeuxCles(KeyPair deuxCles)
    {
        this.deuxCles = deuxCles;
    }

    public PublicKey getClepublique()
    {
        return clepublique;
    }

    public void setClepublique(PublicKey clepublique)
    {
        this.clepublique = clepublique;
    }

    public PrivateKey getCleprivee()
    {
        return cleprivee;
    }

    public void setCleprivee(PrivateKey cleprivee)
    {
        this.cleprivee = cleprivee;
    }
    
    
    
}
