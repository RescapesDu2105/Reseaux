package client_iachat;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;
import requetereponseIACOP.RequeteIACOP;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetereponseIACOP.ReponseIACOP;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doublon
 */
public class Client
{
    private int Port;
    private InetAddress IP;
    private Socket cliSocket = null;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    private Properties Prop = new Properties();
    private String NomUtilisateur;
    private boolean ConnectedToServer;

    public Client() throws IOException
    {
        LireProperties();
    }
   
    public void LireProperties() throws UnknownHostException, IOException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String nomFichier = System.getProperty("user.dir").split("/dist")[0] + System.getProperty("file.separator")+ "src" + System.getProperty("file.separator") + this.getClass().getPackage().getName()+ System.getProperty("file.separator") + "config.properties";
                
        try 
        {
            fis = new FileInputStream(nomFichier);
            getProp().load(fis);
            fis.close();
        } 
        catch (FileNotFoundException ex) 
        {    
            try
            {           
                fis = new FileInputStream(nomFichier);
                getProp().load(fis);
                fis.close();
            } 
            catch (FileNotFoundException ex1) 
            {       
                fos = new FileOutputStream(nomFichier);
                
                getProp().setProperty("PORT_FLY", Integer.toString(30050));
                //getProp().setProperty("ADRESSEIP", "192.168.0.3");
                getProp().setProperty("ADRESSEIP", "127.0.0.1");

                getProp().store(fos, null);
            }
        } 
        
        if (fis != null || fos != null) 
        {
            setPort(Integer.parseInt(getProp().getProperty("PORT_FLY")));            
            setIP(InetAddress.getByName(getProp().getProperty("ADRESSEIP")));
            
            System.out.println("Port : " + getPort());
            System.out.println("IP : " + getIP());
        }
    }
    
    public void Connexion() throws IOException
    {
        try
        {
            setCliSocket(new Socket(getIP(), getPort()));
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        if (getCliSocket().isConnected()) 
        {            
            try 
            {        
                System.out.println("Création des flux");
                setOos(new ObjectOutputStream(getCliSocket().getOutputStream()));
                getOos().flush();
                System.out.println("Fin de la création des flux");
            }
            catch(IOException ex) 
            {
                System.out.println(ex.getMessage());
            }
            setConnectedToServer(true);
        }
        else 
        {            
            
        }
    }
  
    public void Deconnexion() 
    {
        try 
        {
            getOis().close();
            setOis(null);
            getOos().close();
            setOos(null);
            getCliSocket().close();
            setCliSocket(null);            
            setNomUtilisateur("");                
        } 
        catch (IOException ex) 
        {
            //System.exit(1);
        }
        setConnectedToServer(false);
    }
    
    public ReponseIACOP Authenfication(String Login, String Password) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
    {
        RequeteIACOP Req = new RequeteIACOP(RequeteIACOP.REQUEST_LOGIN_GROUP);
        ReponseIACOP Rep = null;
        
        Connexion();        
        
        Security.addProvider(new BouncyCastleProvider());
        Req.getChargeUtile().put("Login", Login);
        MessageDigest md = MessageDigest.getInstance("SHA-256", "BC");
        md.update(Login.getBytes());
        md.update(Password.getBytes()); 
        long Temps = (new Date()).getTime();
        double Random = Math.random();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(Temps); bdos.writeDouble(Random);
        md.update(baos.toByteArray());
        byte[] msgD = md.digest();
        Req.getChargeUtile().put("Temps", Temps);
        Req.getChargeUtile().put("Random", Random);
        Req.getChargeUtile().put("Digest", msgD);         
        EnvoyerRequete(Req);
        Rep = RecevoirReponse();
        
        return Rep;
    }
    
    public void EnvoyerRequete(RequeteIACOP Req)
    {
        try 
        {            
            getOos().writeObject(Req);
            getOos().flush();
        } 
        catch (IOException ex) 
        {
            setConnectedToServer(false);
        }
    }
    
    public ReponseIACOP RecevoirReponse()
    {
        ReponseIACOP Rep = null;
        
        try 
        {
            if (getOis() == null)
                setOis(new ObjectInputStream(getCliSocket().getInputStream()));
            
            Rep = (ReponseIACOP) getOis().readObject();
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            setConnectedToServer(false);
        }
        
        return Rep;
    }
        
    public boolean isConnectedToServer()
    {
        return ConnectedToServer;
    }

    public void setConnectedToServer(boolean ConnectedToServer)
    {
        this.ConnectedToServer = ConnectedToServer;
    }
     
    public Socket getCliSocket()
    {
        return cliSocket;
    }

    public void setCliSocket(Socket cliSocket)
    {
        this.cliSocket = cliSocket;
    }

    public ObjectInputStream getOis()
    {
        return ois;
    }

    public void setOis(ObjectInputStream ois)
    {
        this.ois = ois;
    }

    public ObjectOutputStream getOos()
    {
        return oos;
    }

    public void setOos(ObjectOutputStream oos)
    {
        this.oos = oos;
    }

    public int getPort()
    {
        return Port;
    }

    public void setPort(int Port)
    {
        this.Port = Port;
    }

    public InetAddress getIP()
    {
        return IP;
    }

    public void setIP(InetAddress IP)
    {
        this.IP = IP;
    }
    
    public Properties getProp() 
    {
        return Prop;
    }

    public void setProp(Properties Prop) 
    {
        this.Prop = Prop;
    }
    
    public String getNomUtilisateur() 
    {
        return NomUtilisateur;
    }

    public void setNomUtilisateur(String NomUtilisateur) 
    {
        this.NomUtilisateur = NomUtilisateur;
    }
}
