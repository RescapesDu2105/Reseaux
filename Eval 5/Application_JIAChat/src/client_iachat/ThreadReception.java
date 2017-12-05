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
import javax.swing.JTextArea;

public class ThreadReception extends Thread
{
    private final String nom;
    private final MulticastSocket socketGroupe;
    private final JTextArea Chat;
    
    public ThreadReception (String n, MulticastSocket ms, JTextArea Chat)
    {
        this.nom = n; 
        this.socketGroupe = ms; 
        this.Chat = Chat;
    }
    
    @Override
    public void run()
    {
        boolean enMarche = true;
        while (enMarche)
        {
            try
            {
                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                socketGroupe.receive(dtg);
                Chat.insert(new String (buf).trim(), Chat.getLineCount());
            }
            catch (IOException e)
            {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                enMarche = false; // fin
            }
        }
    }
}