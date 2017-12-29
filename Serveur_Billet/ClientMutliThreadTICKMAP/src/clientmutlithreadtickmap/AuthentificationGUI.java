/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmutlithreadtickmap;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.swing.JOptionPane;
import protocoleTICKMAP.ReponseTICKMAP;

/**
 *
 * @author Doublon
 */
public class AuthentificationGUI extends javax.swing.JFrame
{
    private Client Client;

    /**
     * Creates new form AuthentificationGUI
     */
    public AuthentificationGUI()
    {
        setLocationRelativeTo(null); 
        initComponents();
        this.getRootPane().setDefaultButton(jButtonConnexion);
        
        try 
        {  
            this.Client = new Client();
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(this, "Problème interne au client !", "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
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

        jLabelLogin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jButtonConnexion = new javax.swing.JButton();
        jButtonEffacer = new javax.swing.JButton();
        jPasswordFieldPsw = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelLogin.setText("Nom d'utilisateur :");

        jLabel1.setText("Mot de passe :");

        jButtonConnexion.setText("Connexion");
        jButtonConnexion.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonConnexionActionPerformed(evt);
            }
        });

        jButtonEffacer.setText("Effacer");
        jButtonEffacer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonEffacerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldLogin)
                    .addComponent(jPasswordFieldPsw))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jButtonConnexion)
                .addGap(18, 18, 18)
                .addComponent(jButtonEffacer)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldPsw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConnexion)
                    .addComponent(jButtonEffacer))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonConnexionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonConnexionActionPerformed
    {//GEN-HEADEREND:event_jButtonConnexionActionPerformed
        if (jTextFieldLogin.getText().isEmpty() || jPasswordFieldPsw.getPassword().length == 0)
        {
            JOptionPane.showMessageDialog(this, ReponseTICKMAP.WRONG_USER_PASSWORD_MESSAGE, "Erreur", JOptionPane.ERROR_MESSAGE);
            jPasswordFieldPsw.setText("");
        }
        else
        {
            ReponseTICKMAP Rep = null;
            try 
            {
                Rep = getClient().Authenfication(jTextFieldLogin.getText(), String.valueOf(jPasswordFieldPsw.getPassword()));
                //ReponseLUGAP Rep = Client.Authenfication("Zeydax", "123");
            } 
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(this, "Le serveur est déconnecté !", "Erreur", JOptionPane.ERROR_MESSAGE);
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
            {
                JOptionPane.showMessageDialog(this, "Erreur interne au client !", "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            if (Rep != null) 
            {
                if(Rep.getCode() == ReponseTICKMAP.LOGIN_OK)
                {
                    getClient().setNomUtilisateur(Rep.getChargeUtile().get("Prenom").toString() + " " + (Rep.getChargeUtile().get("Nom").toString()));

                    this.dispose();
                    AuthentificationGUI Test = this;
                    
                   java.awt.EventQueue.invokeLater(() -> {
                        new test().setVisible(true);
                    });
                    this.jButtonEffacerActionPerformed(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, (String) Rep.getChargeUtile().get("Message"), "Erreur", JOptionPane.ERROR_MESSAGE);
                    jPasswordFieldPsw.setText("");
                    getClient().Deconnexion();
                }
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonConnexionActionPerformed

    private void jButtonEffacerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonEffacerActionPerformed
    {//GEN-HEADEREND:event_jButtonEffacerActionPerformed
        jTextFieldLogin.setText("");
        jPasswordFieldPsw.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEffacerActionPerformed

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
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(AuthentificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(AuthentificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(AuthentificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(AuthentificationGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new AuthentificationGUI().setVisible(true);
            }
        });
    }

    public Client getClient()
    {
        return Client;
    }

    public void setClient(Client Client)
    {
        this.Client = Client;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConnexion;
    private javax.swing.JButton jButtonEffacer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JPasswordField jPasswordFieldPsw;
    private javax.swing.JTextField jTextFieldLogin;
    // End of variables declaration//GEN-END:variables
}
