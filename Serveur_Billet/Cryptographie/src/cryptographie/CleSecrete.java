/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Doublon
 */
public class CleSecrete
{
    private static String codeProvider ="BC";
    
    private KeyGenerator cleGen ;
    private SecretKey cle;

    public CleSecrete() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        Security.addProvider(new BouncyCastleProvider());
        cleGen= KeyGenerator.getInstance("DES", codeProvider);
        cle=cleGen.generateKey();
    }
    
    public CleSecrete(SecretKey cleRecue)
    {
        Security.addProvider(new BouncyCastleProvider());
        setCle(cleRecue);
    }
    
    public void SaveCle(String path,String nomFichier) throws IOException
    {
        ObjectOutputStream cleFichier = new ObjectOutputStream(new FileOutputStream(path+nomFichier));
        cleFichier.writeObject(cle);
        cleFichier.close();
    }
    
    public SecretKey LoadCle(String path,String nomFichier) throws IOException, ClassNotFoundException
    {
        System.out.println(path+nomFichier);
        ObjectInputStream cleFichier =new ObjectInputStream(new FileInputStream(path+nomFichier));
        SecretKey keyLoad=(SecretKey) cleFichier.readObject();
        cleFichier.close();
        System.out.println(" *** Clé récupérée = " + getCle().toString());   
        return keyLoad;
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
    
    
}
