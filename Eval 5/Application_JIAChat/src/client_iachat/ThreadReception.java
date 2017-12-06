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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import requetereponseIACOP.RequeteIACOP;

public class ThreadReception extends Thread
{
    private final String nom;
    private final MulticastSocket socketGroupe;
    private final ArrayList<String> Questions;
    private final JTextArea Chat;
    
    public ThreadReception (String n, MulticastSocket ms, ArrayList<String> Questions, JTextArea Chat)
    {
        this.nom = n; 
        this.socketGroupe = ms; 
        this.Chat = Chat;
        this.Questions = Questions;
    }
    
    @Override
    public void run()
    {
        boolean enMarche = true;
        while (enMarche)
        {
            try
            {
                RequeteIACOP.RecevoirMessage(Questions, Chat, socketGroupe);
            }
            catch (IOException e)
            {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                enMarche = false; // fin
            }
            catch (NoSuchAlgorithmException | NoSuchProviderException ex)
            {
                Logger.getLogger(ThreadReception.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}