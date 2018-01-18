/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurmultithreadpayment;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;
import requetepoolsthreadspayment.ConsoleServeur;


/**
 *
 * @author Doublon
 */
public class Serveur extends Thread
{
    private ConsoleServeur GUIApplication;
    private Properties Prop = null;
    private ServerSocket SSocket_Billet= null;
    private int Port_Billet;
    private int MaxClients;

    private ArrayList<ThreadClient> threads = new ArrayList<>();

    public Serveur(ConsoleServeur GUIApplication) 
    {
        this.GUIApplication = GUIApplication;
        try 
        {
            this.Prop = (new ServerProperties()).getProp();
            this.Port_Billet = Integer.parseInt(this.Prop.getProperty("PORT_PAYMENT"));
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
        setSSocket_Billet(new ServerSocket(getPort_Billet()));

        for (int i = 0 ; i < getMaxClients() ; i++) 
        {
            getThreads().add(new ThreadClient("Thread du pool n°" + String.valueOf(i + 1), getSSocket_Billet(), getGUIApplication(), getProp()));
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

                getSSocket_Billet().close();
                System.out.println("Demande d'arrêt du " + getThreads().get(i).getNom());
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur socket ! [" + ex.getMessage() + "]");
            }
        }
    }   

    public Properties getProp()
    {
        return Prop;
    }

    public void setProp(Properties Prop)
    {
        this.Prop = Prop;
    }

    public ServerSocket getSSocket_Billet()
    {
        return SSocket_Billet;
    }

    public void setSSocket_Billet(ServerSocket SSocket_Billet)
    {
        this.SSocket_Billet = SSocket_Billet;
    }

    public int getPort_Billet()
    {
        return Port_Billet;
    }

    public void setPort_Billet(int Port_Billet)
    {
        this.Port_Billet = Port_Billet;
    }

    public int getMaxClients()
    {
        return MaxClients;
    }

    public void setMaxClients(int MaxClients)
    {
        this.MaxClients = MaxClients;
    }

    public ArrayList<ThreadClient> getThreads()
    {
        return threads;
    }

    public void setThreads(ArrayList<ThreadClient> threads)
    {
        this.threads = threads;
    }

    public ConsoleServeur getGUIApplication()
    {
        return GUIApplication;
    }

    public void setGUIApplication(ConsoleServeur GUIApplication)
    {
        this.GUIApplication = GUIApplication;
    }

}
