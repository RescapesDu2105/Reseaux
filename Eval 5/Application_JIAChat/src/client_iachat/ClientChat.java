/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import client_iachat.ThreadReception;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import requetereponseIACOP.RequeteIACOP;
import javax.swing.DefaultListModel;

/**
 *
 * @author Philippe
 */
public class ClientChat extends javax.swing.JFrame
{
    private final String nomPrenomClient;
    private InetAddress adresseGroupe;
    private final int port;
    private MulticastSocket socketGroupe;
    private ThreadReception thread;
    private final ArrayList<String> Questions;
    
    /**
     * Creates new form ClientChat
     * @param nomPrenomClient
     * @param port
     */
    public ClientChat(String nomPrenomClient, int port)
    {        
        this.Questions = new ArrayList<>();
        this.nomPrenomClient = nomPrenomClient;
        this.port = port;
        
        initComponents();        
        setLocationRelativeTo(null); 
        
        Init();                
    }
    
    public void Init()
    {
        try
        {
            adresseGroupe = InetAddress.getByName("234.5.5.9");
            socketGroupe = new MulticastSocket(port);            
            //socketGroupe.setInterface(InetAddress.getLocalHost());
            socketGroupe.joinGroup(adresseGroupe);
            System.out.println("socketGroupe.isConnected() = " + socketGroupe.isConnected());
            thread = new ThreadReception (nomPrenomClient, socketGroupe, Questions, jList_Questions, jTA_Chat);
            thread.start();
            
            this.setTitle("Client : " + nomPrenomClient);
            
            //RequeteIACOP.Connexion(nomPrenomClient, adresseGroupe, port, socketGroupe);
            String msgDeb = nomPrenomClient + " a rejoint le groupe";
            System.out.println("msgDeb = " + msgDeb);
            DatagramPacket dtg = new DatagramPacket(msgDeb.getBytes(), msgDeb.length(), adresseGroupe, port);
            socketGroupe.send(dtg);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        /*catch (UnknownHostException ex)
        {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public void Stop()
    {
        try
        {
            RequeteIACOP.Deconnexion(nomPrenomClient, adresseGroupe, port, socketGroupe);
            socketGroupe.leaveGroup(adresseGroupe);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread.interrupt();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTA_Chat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTA_Envoyer = new javax.swing.JTextArea();
        jButtonEnvoyer = new javax.swing.JButton();
        jCB_Type = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_Questions = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
        });

        jTA_Chat.setEditable(false);
        jTA_Chat.setColumns(20);
        jTA_Chat.setRows(5);
        jScrollPane1.setViewportView(jTA_Chat);

        jTA_Envoyer.setColumns(20);
        jTA_Envoyer.setRows(5);
        jScrollPane2.setViewportView(jTA_Envoyer);

        jButtonEnvoyer.setText("Envoyer");
        jButtonEnvoyer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonEnvoyerActionPerformed(evt);
            }
        });

        jCB_Type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Information", "Question", "Réponse" }));

        jList_Questions.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(jList_Questions);

        jLabel1.setText("Questions");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonEnvoyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCB_Type, 0, 128, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel1)
                        .addGap(0, 40, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCB_Type, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonEnvoyer, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEnvoyerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonEnvoyerActionPerformed
    {//GEN-HEADEREND:event_jButtonEnvoyerActionPerformed
        String msg = null;        
        String Tag = null;
        String MessageToSend = jTA_Envoyer.getText();
        
        jTA_Envoyer.setText(null);
        
        switch(jCB_Type.getSelectedIndex())
        {
            case 0:
                RequeteIACOP.EnvoyerInformation(nomPrenomClient, MessageToSend, adresseGroupe, port, socketGroupe);
                break;
                
            case 1: 
                RequeteIACOP.EnvoyerQuestion(nomPrenomClient, MessageToSend, adresseGroupe, port, socketGroupe);
                break;
                
            case 2:
                String value = jList_Questions.getSelectedValue();
                if (value == null)
                    JOptionPane.showMessageDialog(this, "Veuillez selectionner une question dans la liste si vous désirez répondre à une question", "Erreur", JOptionPane.ERROR_MESSAGE);
                else
                {
                    Tag = "(R" + jList_Questions.getSelectedValue() + ")";                    
                    RequeteIACOP.EnvoyerReponse(nomPrenomClient, Tag, MessageToSend, adresseGroupe, port, socketGroupe);
                }
                break;
        }       
    }//GEN-LAST:event_jButtonEnvoyerActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        Stop();
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(ClientChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(ClientChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(ClientChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(ClientChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
        {
            new ClientChat(null, 30051).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEnvoyer;
    private javax.swing.JComboBox<String> jCB_Type;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList_Questions;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTA_Chat;
    private javax.swing.JTextArea jTA_Envoyer;
    // End of variables declaration//GEN-END:variables
}
