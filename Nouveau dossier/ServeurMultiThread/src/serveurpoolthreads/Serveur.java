/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Philippe
 */
public class Serveur extends Thread{
    private ConsoleServeur GUIApplication;
    private int Port_Bagages;
    private int Port_CheckIN;
    private int MaxClients;
    private ServerSocket SSocket_Bagages = null;
    private ServerSocket SSocket_CheckIN = null;
    private Properties Prop = null;
    
    private ArrayList<ThreadClient> threads = new ArrayList<>();

    
    public Serveur(ConsoleServeur GUIApplication) 
    {
        this.GUIApplication = GUIApplication;
        try 
        {
            this.Prop = (new ServerProperties()).getProp();
            this.Port_Bagages = Integer.parseInt(this.Prop.getProperty("PORT_BAGAGES"));
            this.MaxClients = Integer.parseInt(this.Prop.getProperty("MAX_CLIENTS"));
        } 
        catch (IOException ex) 
        {
            this.GUIApplication.TraceEvenements("serveur#initialisation#failed to read properties file");
            System.exit(1);
        }
    }
       
    
    public void Init() throws IOException
    {  
        setSSocket_Bagages(new ServerSocket(getPort_Bagages()));
        //setSSocket_CheckIN(new ServerSocket(getPort_CheckIN()));
        
        for (int i = 0 ; i < getMaxClients() ; i++) 
        {
            getThreads().add(new ThreadClient("Thread du pool n°" + String.valueOf(i + 1), getSSocket_Bagages(), getGUIApplication(), getProp()));
            getThreads().get(i).start();
        }
    }        
    
    public void Stop() 
    {
        for (int i = 0 ; i < getMaxClients() ; i++) 
        {
            getThreads().get(i).interrupt();
            try 
            {
                if (getThreads().get(i).getCSocket() != null)
                    getThreads().get(i).getCSocket().close();
                
                getSSocket_Bagages().close();
                System.out.println("Demande d'arrêt du " + getThreads().get(i).getNom());
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur socket ! [" + ex.getMessage() + "]");
            }
        }
    }    
        
    
    public ServerSocket getSSocket_Bagages() {
        return SSocket_Bagages;
    }

    public void setSSocket_Bagages(ServerSocket SSocket_Bagages) {
        this.SSocket_Bagages = SSocket_Bagages;
    }

    public ServerSocket getSSocket_CheckIN() {
        return SSocket_CheckIN;
    }

    public void setSSocket_CheckIN(ServerSocket SSocket_CheckIN) {
        this.SSocket_CheckIN = SSocket_CheckIN;
    }
    
    public int getPort_Bagages() {
        return Port_Bagages;
    }

    public void setPort_Bagages(int Port_Bagages) {
        this.Port_Bagages = Port_Bagages;
    }

    public int getPort_CheckIN() {
        return Port_CheckIN;
    }

    public void setPort_CheckIN(int Port_CheckIN) {
        this.Port_CheckIN = Port_CheckIN;
    }
    
    public int getMaxClients() {
        return MaxClients;
    }

    public void setMaxClients(int MaxClients) {
        this.MaxClients = MaxClients;
    }

    public ConsoleServeur getGUIApplication() {
        return GUIApplication;
    }

    public void setGUIApplication(ConsoleServeur GUIApplication) {
        this.GUIApplication = GUIApplication;
    }

    public Properties getProp() {
        return Prop;
    }

    public void setProp(Properties Prop) {
        this.Prop = Prop;
    }

    public ArrayList<ThreadClient> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<ThreadClient> threads) {
        this.threads = threads;
    }
  
}
