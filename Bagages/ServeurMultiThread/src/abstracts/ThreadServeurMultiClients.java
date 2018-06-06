/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abstracts;

import abstracts.AThreadServeur;
import thread_client.ThreadClient;
import interfaces.SourceTaches;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import interfaces.ConsoleServeur;
import interfaces.Requete;

/**
 *
 * @author Philippe
 */
public class ThreadServeurMultiClients extends AThreadServeur
{  
    protected SourceTaches ListeTaches;
    protected int ThreadsPoolSize;
    protected ArrayList<ThreadClient> ThreadsPool;
    protected Properties Properties;
         
    public ThreadServeurMultiClients(int Port, ConsoleServeur GUI, SourceTaches Taches, Properties Properties) 
    {
        super(Port, GUI);        
        this.ListeTaches = Taches;
        this.Properties = Properties;
        this.ThreadsPoolSize = Integer.parseInt(Properties.get("MAX_THREADS").toString());
        this.ThreadsPool = new ArrayList<>(ThreadsPoolSize);        
    }
    
    public void run()
    {
        Init();
        
        Socket CSocket = null;
        ObjectInputStream ois = null;          
        try
        { 
            System.out.println("************ Serveur en attente"); 
            CSocket = SSocket.accept(); 
            GUI.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#Accept#Thread Serveur"); 
            ois = new ObjectInputStream(CSocket.getInputStream());
            System.out.println("ois = " + ois.toString());
        } 
        catch (IOException e) 
        { 
            if(!isInterrupted())
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
                System.exit(1); 
            }
            else
            {
                Stop();
            }                
        } 

        while(!isInterrupted())
        {            
            AjouterTache(CSocket, GUI);            
        }
    }
    
        
    public void Init()
    {         
        try
        {
            setSSocket(new ServerSocket(getPort()));
        }
        catch(IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]");
            System.exit(1);
        }
        
        // Démarrage du pool de Threads         
        for (int i = 0 ; i < getThreadsPoolSize() ; i++) 
        {
            ThreadClient thr = new ThreadClient(getListeTaches(), getSSocket(), getGUI());
            getThreads().add(thr);
            thr.start();
        }
    }        
    
    public void Stop() 
    {
        for (int i = 0 ; i < getThreadsPoolSize() ; i++) 
        {
            try 
            {
                getThreads().get(i).interrupt();                
                getSSocket().close();                
                this.interrupt();
                System.out.println("Demande d'arrêt du " + getThreads().get(i).getNom());
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur socket ! [" + ex.getMessage() + "]");
            }
        }      
    }  
    
    public void AjouterTache(Socket CSocket, ConsoleServeur GUI)
    {
        try 
        { 
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            Requete req = null;
            req = (Requete)ois.readObject();
            System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());                
            GUI.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + Thread.currentThread().getName());
            Runnable travail = req.createRunnable(CSocket, GUI); 

            if (travail != null) 
            { 
                ListeTaches.recordTache(travail); 
                System.out.println("Travail mis dans la file"); 
            } 
            else 
                System.out.println("Pas de mise en file"); 
        } 
        catch (ClassNotFoundException e) 
        { 
            System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            System.exit(1);
        } 
        catch (IOException e) 
        { 
            System.err.println("Erreur : [" + e.getMessage() + "]");
        }             
    }
        
    // Getters - Set
    public int getThreadsPoolSize() {
        return ThreadsPoolSize;
    }

    public void setThreadsPoolSize(int ThreadsPoolSize) {
        this.ThreadsPoolSize = ThreadsPoolSize;
    }

    public ArrayList<ThreadClient> getThreads() {
        return ThreadsPool;
    }

    public void setThreads(ArrayList<ThreadClient> ThreadsPool) {
        this.ThreadsPool = ThreadsPool;
    }

    public SourceTaches getListeTaches()
    {
        return ListeTaches;
    }

    public void setListeTaches(SourceTaches ListeTaches)
    {
        this.ListeTaches = ListeTaches;
    }

    public ArrayList<ThreadClient> getThreadsPool()
    {
        return ThreadsPool;
    }

    public void setThreadsPool(ArrayList<ThreadClient> ThreadsPool)
    {
        this.ThreadsPool = ThreadsPool;
    }

    public Properties getProperties()
    {
        return Properties;
    }

    public void setProperties(Properties Properties)
    {
        this.Properties = Properties;
    }
}
