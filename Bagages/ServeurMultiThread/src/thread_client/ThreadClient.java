package thread_client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import interfaces.SourceTaches;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import interfaces.ConsoleServeur;

/**
 *
 * @author Philippe
 */
public class ThreadClient extends Thread {
    private final SourceTaches tachesAExecuter;
    private String Nom;
    private final ConsoleServeur GUI;
    private final ServerSocket SSocket;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    private Runnable TacheEnCours = null;

    public ThreadClient(SourceTaches tachesAExecuter, ServerSocket SSocket, ConsoleServeur GUI)
    {
        this.tachesAExecuter = tachesAExecuter;
        this.SSocket = SSocket;
        this.GUI = GUI;
    }


    @Override
    public void run()
    {
        boolean isInterrupted = false; // Utilisation d'une variable pour savoir quand le thread est interrompu car la
                                       // variable prédéfinie isInterrupted reste à false quand on rentre dans l'exception
                                       
        Nom = Thread.currentThread().getName();
        GUI.TraceEvenements(SSocket.getInetAddress().toString() + "#Run#" + Nom);
        while(!isInterrupted)
        {
            try
            {
                TacheEnCours = tachesAExecuter.getTache();
                System.out.println("getTache après");
            }
            catch(InterruptedException e)
            {
                isInterrupted = true;
            }         
            if(TacheEnCours != null)
                TacheEnCours.run(); 
        }
        System.out.println(getNom() + " Je m'arrête");
    }
    
    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ConsoleServeur getGUI() {
        return GUI;
    }

    public ServerSocket getSSocket() {
        return SSocket;
    }

    public String getNom() {
        return Nom;
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
