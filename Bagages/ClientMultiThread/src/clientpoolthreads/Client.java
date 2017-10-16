/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientpoolthreads;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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
        
    public void Connexion() throws IOException
    {
        setCliSocket(new Socket(getIP(), getPort()));
        System.out.println("Connexion établie");
        System.out.println("Création des flux");
        setOos(new ObjectOutputStream(new BufferedOutputStream(cliSocket.getOutputStream())));
        getOos().flush();
        setOis(new ObjectInputStream(new BufferedInputStream(cliSocket.getInputStream())));
        System.out.println("Client opérationnel");
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
    
    
}
