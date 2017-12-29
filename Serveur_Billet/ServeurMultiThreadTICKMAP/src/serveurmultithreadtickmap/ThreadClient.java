/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurmultithreadtickmap;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Doublon
 */
public class ThreadClient
{
    private final String Nom;
    private final ConsoleServeur GUIApplication;
    private final ServerSocket SSocket;
    private Socket CSocket = null;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    private Runnable TacheEnCours = null; //Pas utile

    private final Properties Prop;

    public ThreadClient(String Nom, ServerSocket SSocket, ConsoleServeur GUIApplication, Properties Prop)
    {
        this.Nom = Nom;
        this.SSocket = SSocket;
        this.GUIApplication = GUIApplication;
        this.Prop = Prop;
    }
}
