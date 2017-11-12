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
import java.util.Properties;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Reponse;
import requetepoolthreads.Requete;

/**
 *
 * @author Philippe
 */
public class ThreadClient extends Thread {
    private final String Nom;
    private final ConsoleServeur GUIApplication;
    private final ServerSocket SSocket;
    private Socket CSocket = null;
    
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
    private Runnable TacheEnCours = null; //Pas utile
    
    private final Properties Prop;
    
    //private HashMap<String, Object>Tab = null;

    public ThreadClient(String Nom, ServerSocket SSocket, ConsoleServeur GUIApplication, Properties Prop) 
    {
        this.Nom = Nom;
        this.SSocket = SSocket;
        this.GUIApplication = GUIApplication;     
        //this.Tab = new HashMap<>();
        this.Prop = Prop;
    }
    
    
    @Override
    public void run()
    {  
        while(!isInterrupted()) 
        {
            //System.out.println("avant get Tab = " + Tab);
            try 
            {                
                GUIApplication.TraceEvenements("Serveur#En attente#" + getNom());
                //System.out.println("********** Serveur en attente");
                //System.out.println("1 Tab = " + Tab);
                CSocket = SSocket.accept(); // wtf
                System.out.println("CSocket = " + CSocket.isClosed() + " " + CSocket.isConnected());
                //System.out.println("2 Tab = " + Tab);
                setOos(new ObjectOutputStream(this.CSocket.getOutputStream()));
                //System.out.println("********** Serveur après accept()");      
                GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#Accept#" + getNom());
            } 
            catch (IOException ex) 
            {
                if (!ex.getMessage().toUpperCase().equals("SOCKET CLOSED"))
                    System.err.println("Erreur d'accept ! [" + ex.getMessage() + "]");
                this.interrupt();
            }
            
            System.out.println("Test = " + getCSocket() + " et " + !getCSocket().isClosed());
            while (getCSocket() != null && !getCSocket().isClosed())
            {  
                Requete req = RecevoirRequete(); 
                System.out.println("req = " + req);
                if (req != null)
                {
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + getNom());                    
                    
                    this.TacheEnCours = req.createRunnable(getProp());
                    this.TacheEnCours.run();  
                    
                    EnvoyerReponse(CSocket, req.getReponse());
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getReponse().getChargeUtile().get("Message")+ "#" + getNom());
                    if (req.getReponse().getCode() == Reponse.LOG_OUT_OK)
                    {                        
                        try 
                        {
                            CSocket.close();
                        } 
                        catch (IOException ex) 
                        {
                            req.getReponse().setCode(Reponse.INTERNAL_SERVER_ERROR);
                            req.getReponse().getChargeUtile().put("Message", Reponse.INTERNAL_SERVER_ERROR_MESSAGE);
                        }
                    }
                } 
            }
            
            try 
            {
                if (getOis() != null)
                {                    
                    getOis().close();
                    setOis(null);
                }
                
                if (getOos() != null) 
                {                    
                    getOos().close();
                    setOos(null);
                }
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur d'accept ! [" + ex.getMessage() + "]");
            }
            System.out.println("Socket fermée !");
        }
        System.out.println(getNom() + " je m'arrête");
    }
    
    public Requete RecevoirRequete()
    {        
        Requete req;
        
        try 
        {
            if (getOis() == null)
                setOis(new ObjectInputStream(CSocket.getInputStream()));
            
            req = (Requete)ois.readObject();
            System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());               
        } 
        catch (IOException ex) 
        {
            //ex.printStackTrace();
            try 
            {
                CSocket.close();
            } 
            catch (IOException ex1) 
            {
                System.err.println("Erreur socket ! [" + ex1.getMessage() + "]");
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
                System.err.println("Erreur socket ! [" + ex1.getMessage() + "]");
            }
            return null;
        }
        
        return req;
    }   
    
    public void EnvoyerReponse(Socket s, Reponse Rep)
    {
        try 
        {   
            if (oos == null)
                oos = new ObjectOutputStream(s.getOutputStream());
            
            oos.writeObject(Rep); 
            oos.flush();
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur d'envoi de la réponse ! [" + ex.getMessage() + "]");
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

    public ServerSocket getSSocket() {
        return SSocket;
    }

    public Socket getCSocket() {
        return CSocket;
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

    /*public HashMap<String, Object> getTab() {
        return Tab;
    }

    public void setTab(HashMap<String, Object> Tab) {
        this.Tab = Tab;
    }*/

    public Properties getProp() {
        return Prop;
    }
}
