/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurmultithreadtickmap;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Doublon
 */
public class Serveur 
{
    public class Serveur extends Thread
    {
        private ConsoleServeur GUIApplication;
        private int Port_Bagages;
        private int Port_CheckIN;
        private int MaxClients;
        private ServerSocket SSocket_Bagages = null;
        private ServerSocket SSocket_CheckIN = null;
        private Properties Prop = null;

        private ArrayList<ThreadClient> threads = new ArrayList<>();
    }
    
}
