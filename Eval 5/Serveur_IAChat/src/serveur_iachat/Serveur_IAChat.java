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
/**
 *
 * @author Doublon
 */
public class Serveur_IAChat
{
    private ConsoleServeur GUIApplication;
    private int Port_Con;
    private int Port_Fly;
    private ServerSocket SSocket_CON ;
    private Properties Prop = null;
    private Socket CSocket;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private String test;

    public Serveur_IAChat(ConsoleServeur GUIApplication) 
    {
        this.GUIApplication = GUIApplication;
        try 
        {
            this.Prop = (new ServerProperties()).getProp();
            this.Port_Con = 30050;//Integer.parseInt(this.Prop.getProperty("PORT_CON"));
            setSSocket_CON(new ServerSocket(Port_Con));
            CSocket=SSocket_CON.accept();
        } 
        catch (IOException ex) 
        {
            this.GUIApplication.TraceEvenements("serveur#initialisation#failed to read properties file");
            System.exit(1);
        }
        
        try
        {
            this.GUIApplication.TraceEvenements("serveur#initialisation#requeterecue");
            if (getOis() == null)
                setOis(new ObjectInputStream(CSocket.getInputStream()));
            String message = (String) getOis().readObject();
            System.out.println("Message recu : "+message);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(Serveur_IAChat.class.getName()).log(Level.SEVERE, null, ex);
            this.GUIApplication.TraceEvenements("serveur#initialisation#probleme de flux");
           // System.exit(1);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Serveur_IAChat.class.getName()).log(Level.SEVERE, null, ex);
            this.GUIApplication.TraceEvenements("serveur#initialisation#probleme de lecture du message");
        }
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
