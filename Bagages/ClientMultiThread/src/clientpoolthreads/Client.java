/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientpoolthreads;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philippe
 */
public class Client {    
    protected int Port;
    protected InetAddress IP;
    protected Socket cliSocket = null;
    
    protected ObjectInputStream ois = null;
    protected ObjectOutputStream oos = null;
    
    protected Properties Prop = new Properties();
    
    
    public Client() {
        LireProperties();
    }
    
    
    public void LireProperties() {
        FileInputStream fis = null;
        String nomFichier = System.getProperty("user.dir").split("/dist")[0] + System.getProperty("file.separator")+ "src" + System.getProperty("file.separator") + this.getClass().getPackage().getName()+ System.getProperty("file.separator") + "config.properties";
            
        try {
            fis = new FileInputStream(nomFichier);
            getProp().load(fis);
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenAuthentification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FenAuthentification.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("FILS DE PUTE");
        if (fis != null) {
            setPort(Integer.parseInt(getProp().getProperty("PORT_BAGAGES")));
            try {
                setIP(InetAddress.getByName(getProp().getProperty("ADRESSEIP")));
            } catch (UnknownHostException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Port : " + getPort());
            System.out.println("IP : " + getIP());
        }
        else {
            System.out.println("FUCKED UP 3");
            System.exit(1);
        }
        
    }
    
    public void Connexion()
    {
        try {
            setCliSocket(new Socket(getIP(), getPort()));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (getCliSocket().isConnected()) {
            System.out.println("Connexion OK");
            
            try {        
            System.out.println("Création des flux");
                setOos(new ObjectOutputStream(getCliSocket().getOutputStream()));
            //System.out.println("fflush");
                getOos().flush();
            //System.out.println("Avant OIS");
                setOis(new ObjectInputStream(getCliSocket().getInputStream()));
            //System.out.println("Apres OIS");
            System.out.println("Fin de la création des flux");
            }
            catch(IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Client prêt");
        }
        else {            
            System.out.println("Client pas prêt !");
        }
    }

    public void Deconnexion() {
        try {
            getCliSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
   
    
    // Getters - Setters
    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress IP) {
        this.IP = IP;
    }

    public Socket getCliSocket() {
        return cliSocket;
    }

    public void setCliSocket(Socket cliSocket) {
        this.cliSocket = cliSocket;
    }

    public Properties getProp() {
        return Prop;
    }

    public void setProp(Properties Prop) {
        this.Prop = Prop;
    }
    
    
}
