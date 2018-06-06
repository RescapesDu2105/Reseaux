/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abstract_classes;

import interfaces.ConsoleServeur;
import java.net.ServerSocket;

/**
 *
 * @author phil3
 */
public abstract class AThreadServeur extends Thread
{    
    protected int Port;
    protected ServerSocket SSocket;
    protected ConsoleServeur GUI;

    protected AThreadServeur(int Port, ConsoleServeur GUI) 
    {
        this.GUI = GUI;
        this.Port = Port;        
    }
    
    
    public abstract void Init();
    public abstract void Stop();
    
    // Getters - Setters
    public ConsoleServeur getGUI()
    {
        return GUI;
    }

    public void setGUI(ConsoleServeur GUI)
    {
        this.GUI = GUI;
    }

    public int getPort()
    {
        return Port;
    }

    public void setPort(int Port)
    {
        this.Port = Port;
    }

    public ServerSocket getSSocket()
    {
        return SSocket;
    }

    public void setSSocket(ServerSocket SSocket)
    {
        this.SSocket = SSocket;
    }
}
