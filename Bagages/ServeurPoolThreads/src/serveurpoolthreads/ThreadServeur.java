/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.System.getProperties;
import java.net.ServerSocket;
import java.net.Socket;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class ThreadServeur extends Thread{
    private int Port;
    private SourceTaches TachesAFaire;
    private ConsoleServeur GUIApplication;
    private ServerSocket SSocket = null;
    private int MaxClients;

    
    public ThreadServeur(int Port, int MaxClients, SourceTaches TachesAFaire, ConsoleServeur GUIApplication) {
        setPort(Port);
        setMaxClients(MaxClients);
        setTachesAFaire(TachesAFaire);
        setGUIApplication(GUIApplication);
    }
       
    
    @Override
    public void run() {
        try {
            SSocket = new ServerSocket(getPort());
        } 
        catch (IOException ex) {
            System.err.println("Erreur de port d'écoute ! [" + ex + "]");
            System.exit(1);
        }
        
        for (int i = 0 ; i < getMaxClients() ; i++) {
            ThreadClient thr = new ThreadClient(getTachesAFaire(), "Thread du pool n°" + String.valueOf(i));
            thr.start();
        }
        
        Socket CSocket = null;
        
        while(!isInterrupted()) {
            try {
            System.out.println("********** Serveur en attente");
                CSocket = SSocket.accept();
                GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#accept#thread serveur");
            } catch (IOException ex) {
                System.err.println("Erreur d'accept ! [" + ex.getMessage() + "]");
                System.exit(1);
            }
        }
        
        ObjectInputStream ois = null;
        Requete req = null;
        
        try {
            ois = new ObjectInputStream(CSocket.getInputStream());
            req = (Requete)ois.readObject();
            System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());
        } catch (IOException ex) {
            System.err.println("Erreur ! [" + ex.getMessage() + "]");
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de definition de classe ! [" + ex.getMessage() + "]");
        }
        
        Runnable travail = req.createRunnable(CSocket, GUIApplication);
        if(travail != null){
            TachesAFaire.recordTache(travail);
            System.out.println("Travail mis dans la file");
        }
        else {
            System.out.println("Pas de mise en file !");
        }
    }    

    public int getMaxClients() {
        return MaxClients;
    }

    public void setMaxClients(int MaxClients) {
        this.MaxClients = MaxClients;
    }

    public final int getPort() {
        return Port;
    }

    public final void setPort(int Port) {
        this.Port = Port;
    }

    public SourceTaches getTachesAFaire() {
        return TachesAFaire;
    }

    public void setTachesAFaire(SourceTaches TachesAFaire) {
        this.TachesAFaire = TachesAFaire;
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
    
    
}
