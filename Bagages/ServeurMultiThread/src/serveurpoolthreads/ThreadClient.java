/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class ThreadClient extends Thread {
    private String Nom;
    private ConsoleServeur GUIApplication;
    private ServerSocket SSocket = null;
    private Socket CSocket = null;
    
    private ObjectInputStream ois;
    private ObjectOutputStream oos = null;
    private Requete req = null;
    
    private Runnable TacheEnCours;

    public ThreadClient(String Nom, ServerSocket SSocket, ConsoleServeur GUIApplication) {
        System.out.println("Test 1");
        this.ois = null;
        this.oos = null;
        this.req = null;
        this.Nom = Nom;
        this.SSocket = SSocket;
        this.GUIApplication = GUIApplication;
        System.out.println("Test 2");
    }

    @Override
    public void run(){  
        while(!isInterrupted()) 
        {
            System.out.println(getNom() + " avant get");
            try 
            {                
                GUIApplication.TraceEvenements("serveur#en attente#" + getNom());
                System.out.println("********** Serveur en attente");
                CSocket = SSocket.accept();
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
                try 
                {
                    ois = new ObjectInputStream(CSocket.getInputStream());
                    req = (Requete)ois.readObject();
                    System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());                     
                    
                    this.TacheEnCours = req.createRunnable(CSocket, GUIApplication);
                    this.TacheEnCours.run();      
                } 
                catch (IOException ex) 
                {
                    System.err.println("Erreur ! [" + ex.getMessage() + "]");
                    try 
                    {
                        CSocket.close();
                    } 
                    catch (IOException ex1) 
                    {
                        System.err.println("Erreur ! [" + ex.getMessage() + "]");
                    }
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
                        System.err.println("Erreur ! [" + ex.getMessage() + "]");
                    }
                }
            }
        }
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

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public Requete getReq() {
        return req;
    }

    public void setReq(Requete req) {
        this.req = req;
    }
    
    

    public Runnable getTacheEnCours() {
        return TacheEnCours;
    }

    public void setTacheEnCours(Runnable TacheEnCours) {
        this.TacheEnCours = TacheEnCours;
    }
}
