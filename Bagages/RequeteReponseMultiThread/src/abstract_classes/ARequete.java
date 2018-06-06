/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abstract_classes;

import interfaces.ConsoleServeur;
import interfaces.Requete;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author phil3
 */
public abstract class ARequete implements Serializable, Requete
{    
    public final static int REQUEST_LOG_OUT = 0;
    public final static int REQUEST_LOGIN = 1;
    
    protected int Type;
    protected HashMap<String, Object> chargeUtile;
    
    protected ObjectOutputStream oos;
    protected Socket SocketClient;
    protected ConsoleServeur GUI;
    
    protected AReponse Reponse = null;
    protected Properties Prop = null;

    protected ARequete(int Type, HashMap chargeUtile) 
    {
        this.Type = Type;
        this.chargeUtile = chargeUtile;
        this.oos = null;
        this.Reponse = null;
    }
    
    protected ARequete(int Type) 
    {
        this.Type = Type;
        this.chargeUtile = new HashMap<>();
        this.oos = null;
        this.Reponse = null;
    }
    
    protected void traiterRequeteInconnueOuErreur()
    {
        System.out.println("Requete inconnue ou erreur");
        GUI.TraceEvenements(SocketClient.getRemoteSocketAddress().toString() + "#Requete inconnue ou erreur#" + Thread.currentThread().getName());
    }
    
    protected void EnvoyerReponse()
    {
        try
        {      
            if(oos == null)
                oos = new ObjectOutputStream(SocketClient.getOutputStream());
            oos.writeObject(Reponse); 
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
    
    
    // Getters - Setters    

    public int getType()
    {
        return Type;
    }

    public void setType(int Type)
    {
        this.Type = Type;
    }

    public HashMap<String, Object> getChargeUtile()
    {
        return chargeUtile;
    }

    public void setChargeUtile(HashMap<String, Object> chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }

    public ObjectOutputStream getOos()
    {
        return oos;
    }

    public void setOos(ObjectOutputStream oos)
    {
        this.oos = oos;
    }

    public Socket getSocketClient()
    {
        return SocketClient;
    }

    public void setSocketClient(Socket SocketClient)
    {
        this.SocketClient = SocketClient;
    }

    public ConsoleServeur getGUI()
    {
        return GUI;
    }

    public void setGUI(ConsoleServeur GUI)
    {
        this.GUI = GUI;
    }

    public AReponse getReponse()
    {
        return Reponse;
    }

    public void setReponse(AReponse Reponse)
    {
        this.Reponse = Reponse;
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
