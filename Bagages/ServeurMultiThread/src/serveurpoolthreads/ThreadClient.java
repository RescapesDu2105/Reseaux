/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import ProtocoleLUGAP.ReponseLUGAP;
import ProtocoleLUGAP.RequeteLUGAP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Philippe
 */
public class ThreadClient extends Thread {
    private String Nom;
    private ConsoleServeur GUIApplication;
    private ServerSocket SSocket = null;
    private Socket CSocket = null;
    
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
    private Runnable TacheEnCours;

    public ThreadClient(String Nom, ServerSocket SSocket, ConsoleServeur GUIApplication) 
    {
        this.Nom = Nom;
        this.SSocket = SSocket;
        this.GUIApplication = GUIApplication;        
    }
    
    
    @Override
    public void run()
    {  
        while(!isInterrupted()) 
        {
            System.out.println(getNom() + " avant get");
            try 
            {                
                GUIApplication.TraceEvenements("serveur#en attente#" + getNom());
                System.out.println("********** Serveur en attente");
                CSocket = SSocket.accept();
                setOos(new ObjectOutputStream(this.CSocket.getOutputStream()));
                System.out.println("********** Serveur après accept()");                
                GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#accept#" + getNom());
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur d'accept ! [" + ex.getMessage() + "]");
                this.interrupt();
            }

            while (!getCSocket().isClosed())
            {  
                RequeteLUGAP req = RecevoirRequete();  
                
                if (req != null)
                {
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + getNom());
                    
                    this.TacheEnCours = req.createRunnable(CSocket, GUIApplication);
                    this.TacheEnCours.run();  
                    
                    EnvoyerReponse(CSocket, req.getRep());
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getRep().getCode() + "#" + getNom());
                }
            }
            
            try 
            {
                getOis().close();
                setOis(null);
                getOos().close();
                setOos(null);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Socket fermée !");
        }
    }
    
    public RequeteLUGAP RecevoirRequete()
    {        
        RequeteLUGAP req = null;
        
        try 
        {
            if (getOis() == null)
                setOis(new ObjectInputStream(CSocket.getInputStream()));
            
            req = (RequeteLUGAP)ois.readObject();
            System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());               
        } 
        catch (IOException ex) 
        {
            
            //System.err.println("Erreur flux requête ! [" + ex.getMessage() + "]");
            //ex.printStackTrace();
            try 
            {
                CSocket.close();
            } 
            catch (IOException ex1) 
            {
                System.err.println("Erreur socket ! [" + ex.getMessage() + "]");
            }
            return null;
        } 
        catch (ClassNotFoundException ex) 
        {
            System.err.println("Erreur de definition de classe ! [" + ex.getMessage() + "]");
            try 
            {
                CSocket.close();
            } 
            catch (IOException ex1) 
            {
                System.err.println("Erreur socket ! [" + ex.getMessage() + "]");
            }
            return null;
        }
        
        return req;
    }   
    
    public void EnvoyerReponse(Socket s, ReponseLUGAP Rep)
    {
        try {   
            if (oos == null)
                oos = new ObjectOutputStream(s.getOutputStream());
            
            oos.writeObject(Rep); 
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RequeteLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ConsoleServeur getGUIApplication() {
        return GUIApplication;
    }

    public void setGUIApplication(ConsoleServeur GUIApplication) {
        this.GUIApplication = GUIApplication;
    }

    public ServerSocket getSSocket() {
        return SSocket;
    }

    public void setSSocket(ServerSocket SSocket) {
        this.SSocket = SSocket;
    }

    public Socket getCSocket() {
        return CSocket;
    }

    public void setCSocket(Socket CSocket) {
        this.CSocket = CSocket;
    }
        
    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public Runnable getTacheEnCours() {
        return TacheEnCours;
    }

    public void setTacheEnCours(Runnable TacheEnCours) {
        this.TacheEnCours = TacheEnCours;
    }
}
