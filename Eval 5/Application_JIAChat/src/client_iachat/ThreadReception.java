/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_iachat;

/**
 *
 * @author Philippe
 */
import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JTextArea;
import requetereponseIACOP.RequeteIACOP;

public class ThreadReception extends Thread
{
    private final MulticastSocket socketGroupe;
    private final ArrayList<String> Questions;
    private final JList QList;
    private final JTextArea Chat;
    
    public ThreadReception (MulticastSocket ms, ArrayList<String> Questions, JList QList, JTextArea Chat)
    {
        this.socketGroupe = ms; 
        this.Chat = Chat;
        this.Questions = Questions;
        this.QList = QList;
    }
    
    @Override
    public void run()
    {
        boolean enMarche = true;
        while (!isInterrupted())
        //while (enMarche)
        {
            try
            {
                RequeteIACOP.RecevoirMessage(Questions, QList, Chat, socketGroupe);
            }
            catch (IOException e)
            {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                e.printStackTrace();
                enMarche = false; // fin            
                interrupt();
            }
            catch (NoSuchAlgorithmException | NoSuchProviderException | ClassNotFoundException ex)
            {
                Logger.getLogger(ThreadReception.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
}