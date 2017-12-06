/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur_iachat;

import reponserequetemonothread.ConsoleServeur;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import reponserequetemonothread.Reponse;
import reponserequetemonothread.Requete;
/**
 *
 * @author Doublon
 */
public class Serveur_IAChat extends Thread
{        
    private ConsoleServeur GUIApplication;
    private int Port_Con;
    private int Port_Fly;
    private ServerSocket SSocket_CON ;
    private Properties Prop = null;
    private Socket CSocket;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;    

    public Serveur_IAChat(ConsoleServeur GUIApplication)
    {
        try
        {
            this.GUIApplication = GUIApplication;
            this.Prop = (new ServerProperties()).getProp();
            this.Port_Con = Integer.parseInt(this.Prop.getProperty("PORT_CON"));
            setSSocket_CON(new ServerSocket(Port_Con));
        }
        catch (IOException ex)
        {
            Logger.getLogger(Serveur_IAChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {  
        while(!isInterrupted()) 
        {            
            try 
            {
                GUIApplication.TraceEvenements("ThreadServeur#En attente#");
                CSocket=SSocket_CON.accept();
                GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#Accept#");
            } 
            catch (IOException ex) 
            {
                if (!ex.getMessage().equals("socket closed"))
                    this.GUIApplication.TraceEvenements("serveur#initialisation#failed to read properties file");
                
                this.interrupt();
            }

            System.out.println("CSocket = " + CSocket.isClosed());
            
            if (getCSocket() != null && !getCSocket().isClosed())
            {               
                System.out.println("test 1");
                Requete req = RecevoirRequete(); 
                System.out.println("test 2");
                if (req != null)
                {
                    
                System.out.println("test 3");
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getNomTypeRequete() + "#" + "Thread");                    
                    
                    Runnable runnable = req.createRunnable(getProp());
                    runnable.run();  
                    
                    EnvoyerReponse(CSocket, req.getReponse());
                    GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#" + req.getReponse().getChargeUtile().get("Message")+ "#" + "Thread");
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
            
            if(getCSocket() != null)
                getCSocket().close();            
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur d'accept ! [" + ex.getMessage() + "]");
        }
        System.out.println("Socket fermée !");
    }
    

    public Requete RecevoirRequete()
    {        
        Requete req;
        
        try 
        {
            setOis(new ObjectInputStream(CSocket.getInputStream()));
            
            req = (Requete)ois.readObject();
            System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());               
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
        
        setOis(null);
        
        return req;
    }   
    
    public void EnvoyerReponse(Socket s, Reponse Rep)
    {
        try 
        {   
            setOos(new ObjectOutputStream(s.getOutputStream()));
            
            getOos().writeObject(Rep); 
            getOos().flush();
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur d'envoi de la réponse ! [" + ex.getMessage() + "]");
        }
        
        setOos(null);
    }
    
    public Socket getCSocket()
    {
        return CSocket;
    }

    public void setCSocket(Socket CSocket)
    {
        this.CSocket = CSocket;
    }
    
    public ObjectInputStream getOis()
    {
        return ois;
    }

    public void setOis(ObjectInputStream ois)
    {
        this.ois = ois;
    }

    public ObjectOutputStream getOos()
    {
        return oos;
    }

    public void setOos(ObjectOutputStream oos)
    {
        this.oos = oos;
    }
    
    public ConsoleServeur getGUIApplication()
    {
        return GUIApplication;
    }

    public void setGUIApplication(ConsoleServeur GUIApplication)
    {
        this.GUIApplication = GUIApplication;
    }

    public int getPort_Con()
    {
        return Port_Con;
    }

    public void setPort_Con(int Port_Con)
    {
        this.Port_Con = Port_Con;
    }

    public int getPort_Fly()
    {
        return Port_Fly;
    }

    public void setPort_Fly(int Port_Fly)
    {
        this.Port_Fly = Port_Fly;
    }

    public ServerSocket getSSocket_CON()
    {
        return SSocket_CON;
    }

    public void setSSocket_CON(ServerSocket SSocket_CON)
    {
        this.SSocket_CON = SSocket_CON;
    }

    public Properties getProp()
    {
        return Prop;
    }

    public void setProp(Properties Prop)
    {
        this.Prop = Prop;
    }
      
}
