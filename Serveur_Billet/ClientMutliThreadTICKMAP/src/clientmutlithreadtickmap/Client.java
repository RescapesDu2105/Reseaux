/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmutlithreadtickmap;

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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocolePAYP.ReponsePAYP;
import protocolePAYP.RequetePAYP;
import protocoleTICKMAP.ReponseTICKMAP;
import protocoleTICKMAP.RequeteTICKMAP;

/**
 *
 * @author Doublon
 */
public class Client
{
    private int PortBagage;
    private int PortPayment;
    private InetAddress IP;
    private Socket cliSocket = null;
    
    private ObjectInputStream oisTICKMAP = null;
    private ObjectOutputStream oosTICKMAP = null;
    
    private ObjectInputStream oisPAYP = null;
    private ObjectOutputStream oosPAYP = null;
    
    private Properties Prop = new Properties();
    
    private String NomUtilisateur;
    private boolean ConnectedToServer;
    private boolean ConnectedToServerPAYP;
    
    public Client() throws IOException, UnknownHostException
    {
        LireProperties();
    }
    
    public ReponseTICKMAP Authenfication(String Login, String Password) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_LOGIN_PORTER);
        ReponseTICKMAP Rep = null;
        
        System.out.println();
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
    
    public void Connexion() throws IOException
    {
        setCliSocket(new Socket(getIP(), getPortBagage()));
        
        if (getCliSocket().isConnected()) 
        {
            System.out.println("Connexion OK");
            
            try 
            {        
                System.out.println("Création des flux");
                setOosTICKMAP(new ObjectOutputStream(getCliSocket().getOutputStream()));
                getOosTICKMAP().flush();
                System.out.println("Fin de la création des flux");
            }
            catch(IOException ex) 
            {
                System.out.println(ex.getMessage());
            }
            System.out.println("Client prêt");
            System.out.println("Connected = " + getCliSocket().isConnected());
            setConnectedToServer(true);
        }
        else 
        {            
            System.out.println("Client pas prêt !");
        }
    }
    
    public void ConnexionPAYP() throws IOException
    {
        setCliSocket(new Socket(getIP(), getPortPayment()));
        
        if (getCliSocket().isConnected()) 
        {
            System.out.println("Connexion OK");
            
            try 
            {        
                System.out.println("Création des flux");
                setOosPAYP(new ObjectOutputStream(getCliSocket().getOutputStream()));
                getOosPAYP().flush();
                System.out.println("Fin de la création des flux");
            }
            catch(IOException ex) 
            {
                System.out.println(ex.getMessage());
            }
            System.out.println("Connected = " + getCliSocket().isConnected());
            setConnectedToServerPAYP(true);
        }
        else 
        {            
            System.out.println("Client pas prêt !");
        }
    }

    public String Deconnexion() 
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_LOG_OUT_PORTER);
        ReponseTICKMAP Rep;
        
        EnvoyerRequete(Req);
        Rep = RecevoirReponse();
        
        if (Rep != null)
        {
            if (Rep.getCode() == ReponseTICKMAP.LOG_OUT_OK)
            {
                try 
                {
                    getOisTICKMAP().close();
                    setOisTICKMAP(null);
                    getOosTICKMAP().close();
                    setOosTICKMAP(null);
                    getCliSocket().close();
                    setCliSocket(null);            
                    setNomUtilisateur("");   
                    
                    getOosPAYP().close();
                    setOosPAYP(null);
                    getOisPAYP().close();
                    setOisPAYP(null);
                } 
                catch (IOException ex) 
                {
                    System.exit(1);
                }
                setConnectedToServer(false);

                return null;
            }
            else
            {
                return Rep.getChargeUtile().get("Message").toString();
            }
        }
        else
        {
            setConnectedToServer(false);
            return null;
        }
    }
    
    public void EnvoyerRequete(RequeteTICKMAP Req)
    {
        try 
        {            
            getOosTICKMAP().writeObject(Req);
            getOosTICKMAP().flush();
        } 
        catch (IOException ex) 
        {
            setConnectedToServer(false);
        }
    }
    
    public void EnvoyerRequete(RequetePAYP Req)
    {
        try 
        {            
            getOosPAYP().writeObject(Req);
            getOosPAYP().flush();
        } 
        catch (IOException ex) 
        {
            setConnectedToServerPAYP(false);
        }
    }
    
    public ReponseTICKMAP RecevoirReponse()
    {
        ReponseTICKMAP Rep = null;
        
        try 
        {
            if (getOisTICKMAP() == null)
                setOisTICKMAP(new ObjectInputStream(getCliSocket().getInputStream()));
            
            Rep = (ReponseTICKMAP) getOisTICKMAP().readObject();
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            setConnectedToServer(false);
        }
        
        return Rep;
    }
    
    public ReponsePAYP RecevoirReponsePAYP()
    {
        ReponsePAYP Rep = null;
        
        try 
        {
            if (getOisPAYP() == null)
                setOisPAYP(new ObjectInputStream(getCliSocket().getInputStream()));
            
            Rep = (ReponsePAYP) getOisPAYP().readObject();
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            setConnectedToServerPAYP(false);
        }
        
        return Rep;
    }
    
    public void LireProperties() throws UnknownHostException, IOException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String nomFichier = System.getProperty("user.dir").split("\\\\dist")[0] + System.getProperty("file.separator")+ "src" + System.getProperty("file.separator") + this.getClass().getPackage().getName()+ System.getProperty("file.separator") + "config.properties";
                
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
                nomFichier = System.getProperty("user.dir") + System.getProperty("file.separator") + "config.properties";
                
                fis = new FileInputStream(nomFichier);
                getProp().load(fis);
                fis.close();
            } 
            catch (FileNotFoundException ex1) 
            {       
                fos = new FileOutputStream(nomFichier);
                
                getProp().setProperty("PORT_BAGAGES", Integer.toString(30070));
                getProp().setProperty("PORT_PAYMENT", Integer.toString(30080));
                getProp().setProperty("ADRESSEIP", "192.168.0.3");

                getProp().store(fos, null);
            }
        } 
        
        if (fis != null || fos != null) 
        {
            setPortBagage(Integer.parseInt(getProp().getProperty("PORT_BAGAGES")));
            setPortPayment(Integer.parseInt(getProp().getProperty("PORT_PAYMENT")));            
            setIP(InetAddress.getByName(getProp().getProperty("ADRESSEIP")));
            
            System.out.println("PortBagage : " + getPortBagage());
            System.out.println("PortPayment : "+getPortPayment());
            System.out.println("IP : " + getIP());
        }
    }

    public int getPortBagage()
    {
        return PortBagage;
    }

    public void setPortBagage(int Port)
    {
        this.PortBagage = Port;
    }

    public InetAddress getIP()
    {
        return IP;
    }

    public void setIP(InetAddress IP)
    {
        this.IP = IP;
    }

    public Socket getCliSocket()
    {
        return cliSocket;
    }

    public void setCliSocket(Socket cliSocket)
    {
        this.cliSocket = cliSocket;
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

    public boolean isConnectedToServer()
    {
        return ConnectedToServer;
    }

    public void setConnectedToServer(boolean ConnectedToServer)
    {
        this.ConnectedToServer = ConnectedToServer;
    }

    public int getPortPayment()
    {
        return PortPayment;
    }

    public void setPortPayment(int PortPayment)
    {
        this.PortPayment = PortPayment;
    }

    public boolean isConnectedToServerPAYP()
    {
        return ConnectedToServerPAYP;
    }

    public void setConnectedToServerPAYP(boolean ConnectedToServerPAYP)
    {
        this.ConnectedToServerPAYP = ConnectedToServerPAYP;
    }

    public ObjectInputStream getOisTICKMAP()
    {
        return oisTICKMAP;
    }

    public void setOisTICKMAP(ObjectInputStream oisTICKMAP)
    {
        this.oisTICKMAP = oisTICKMAP;
    }

    public ObjectOutputStream getOosTICKMAP()
    {
        return oosTICKMAP;
    }

    public void setOosTICKMAP(ObjectOutputStream oosTICKMAP)
    {
        this.oosTICKMAP = oosTICKMAP;
    }

    public ObjectInputStream getOisPAYP()
    {
        return oisPAYP;
    }

    public void setOisPAYP(ObjectInputStream oisPAYP)
    {
        this.oisPAYP = oisPAYP;
    }

    public ObjectOutputStream getOosPAYP()
    {
        return oosPAYP;
    }

    public void setOosPAYP(ObjectOutputStream oosPAYP)
    {
        this.oosPAYP = oosPAYP;
    }
    
    
   
}
