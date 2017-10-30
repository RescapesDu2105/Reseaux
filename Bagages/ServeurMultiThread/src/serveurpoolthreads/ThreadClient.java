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
import java.util.HashMap;
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
    
    private Runnable TacheEnCours; //Pas utile
    
    private HashMap<String, Object>Tab = null;

    public ThreadClient(String Nom, ServerSocket SSocket, ConsoleServeur GUIApplication) 
    {
        this.Nom = Nom;
        this.SSocket = SSocket;
        this.GUIApplication = GUIApplication;     
        this.Tab = new HashMap<>();
    }
    
    
    @Override
    public void run()
    {  
        while(!isInterrupted()) 
        {
            System.out.println("avant get Tab = " + Tab);
            System.out.println(getNom() + " avant get");
            try 
            {                
                GUIApplication.TraceEvenements("Serveur#En attente#" + getNom());
                //System.out.println("********** Serveur en attente");
                System.out.println("1 Tab = " + Tab);
                CSocket = SSocket.accept(); // wtf
                System.out.println("2 Tab = " + Tab);
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

            while (getCSocket() != null && !getCSocket().isClosed())
            {  
                RequeteLUGAP req = RecevoirRequete();  
                if (req != null)
                {
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + getNom());                    
                    this.TacheEnCours = req.createRunnable();
                    //this.GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + getNom());
                    //this.TacheEnCours = req.createRunnable(getTab());
                    this.TacheEnCours.run();  
                    
                    EnvoyerReponse(CSocket, req.getRep());
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getRep().getChargeUtile().get("Message")+ "#" + getNom());
                    if (req.getRep().getCode() == ReponseLUGAP.LOG_OUT_OK)
                    {                        
                        try 
                        {
                            CSocket.close();
                        } 
                        catch (IOException ex) 
                        {
                            req.getRep().setCodeRetour(ReponseLUGAP.INTERNAL_SERVER_ERROR);
                            req.getRep().getChargeUtile().put("Message", ReponseLUGAP.INTERNAL_SERVER_ERROR_MESSAGE);
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
    
    public RequeteLUGAP RecevoirRequete()
    {        
        RequeteLUGAP req = null;
        
        try 
        {
            if (getOis() == null)
                setOis(new ObjectInputStream(CSocket.getInputStream()));
            
            req = (RequeteLUGAP)ois.readObject();
            //System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());               
        } 
        catch (IOException ex) 
        {
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
    
    public void EnvoyerReponse(Socket s, ReponseLUGAP Rep)
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

    public HashMap<String, Object> getTab() {
        return Tab;
    }

    public void setTab(HashMap<String, Object> Tab) {
        this.Tab = Tab;
    }

}
