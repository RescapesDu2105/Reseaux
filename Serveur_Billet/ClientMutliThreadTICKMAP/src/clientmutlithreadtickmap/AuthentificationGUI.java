/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmutlithreadtickmap;

import cryptographie.Certificats;
import cryptographie.CleSecrete;
import cryptographie.ClesPourCryptageAsymetrique;
import cryptographie.CryptageAsymetrique;
import cryptographie.KeyStoreUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
import protocoleTICKMAP.ReponseTICKMAP;
import protocoleTICKMAP.RequeteTICKMAP;
import static protocoleTICKMAP.RequeteTICKMAP.REQUEST_SEND_CERTIFICATE;
import static protocoleTICKMAP.RequeteTICKMAP.REQUEST_SEND_SYMETRIC_KEY;

/**
 *
 * @author Doublon
 */
public class AuthentificationGUI extends javax.swing.JFrame
{
    private static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ClientKeystore.jks";
    private static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private static String keySecretHmac = "CleSecreteHMACClient.ser";
    private static String keyStorePsw = "123Soleil";
    private static String aliasKeyStrore="clientprivatekey";
    
    private Client Client;
    private KeyStoreUtils ks;
    private X509Certificate certifServeur;
    private CleSecrete cleHMAC=null;

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

                    
                    RequestSendCertificate(keyStorePath,keyStorePsw,aliasKeyStrore);
                    RequestSendSecretKey(keyStoreDirPath,keySecretHmac);
                    
                    this.dispose();
                    AuthentificationGUI Test = this;
                    /*cles=new ClesPourCryptageAsymetrique();
                    cles.SerialiserCle();*/
                    
                   java.awt.EventQueue.invokeLater(() -> {
                        new ListeVolsGUI(getClient()).setVisible(true);
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
        }
    }//GEN-LAST:event_jButtonConnexionActionPerformed

    private void jButtonEffacerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonEffacerActionPerformed
    {//GEN-HEADEREND:event_jButtonEffacerActionPerformed
        jTextFieldLogin.setText("");
        jPasswordFieldPsw.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEffacerActionPerformed

    
    public void RequestSendCertificate(String keystorelocation, String psw , String alias )
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_SEND_CERTIFICATE);
        ReponseTICKMAP Rep = null;
        
        try
        {
            ks=new KeyStoreUtils(keystorelocation,psw,alias);            
        } catch (KeyStoreException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Req.getChargeUtile().put("Certificate" , ks.getCertif());
        Client.EnvoyerRequete(Req);
        Rep = Client.RecevoirReponse();
        if(Rep.getCode() == ReponseTICKMAP.SEND_CERTIFICATE_OK)
        {
            certifServeur=(X509Certificate) Rep.getChargeUtile().get("Certificate");
            
            System.out.println("");
            System.out.println("Reception du certificat du serveur");
            System.out.println("Classe instanciée : " + certifServeur.getClass().getName());
            System.out.println("Type de certificat : " + certifServeur.getType());
            System.out.println("Nom du propriétaire du certificat : " +certifServeur.getSubjectDN().getName());
            System.out.println("Dates limites de validité : [" + certifServeur.getNotBefore() + " - " +certifServeur.getNotAfter() + "]");   


            System.out.println("... sa clé publique : " + certifServeur.getPublicKey().toString());
            System.out.println("... la classe instanciée par celle-ci : " +certifServeur.getPublicKey().getClass().getName());
        }
    }
    
    public void RequestSendSecretKey(String path, String nameFile)
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_SEND_SYMETRIC_KEY);
        ReponseTICKMAP Rep = null;
        try
        {
            File f = new File(path+nameFile);
            if(f.exists())
            {
                try
                {
                    ObjectInputStream cleFichier =new ObjectInputStream(new FileInputStream(path+nameFile));
                    SecretKey keyLoad=(SecretKey) cleFichier.readObject();
                    cleFichier.close();
                    cleHMAC=new CleSecrete(keyLoad);
                } catch (IOException ex)
                {
                    Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                try
                {
                    cleHMAC=new CleSecrete();
                    cleHMAC.SaveCle(path, nameFile);
                } catch (NoSuchAlgorithmException ex)
                {
                    Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchProviderException ex)
                {
                    Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            CryptageAsymetrique cryptage = new CryptageAsymetrique();
            //byte[] cleACrypte = cleHMAC.toString().getBytes();
            System.out.println("");
            PublicKey clePubliqueServer = certifServeur.getPublicKey();
            System.out.println("Cle : " +clePubliqueServer.toString());
            byte[] cleByte = cleHMAC.getCle().getEncoded();
            //byte[] cleByte = "bonjour".getBytes();
            byte[] cleCrypte=cryptage.Crypte(certifServeur.getPublicKey(), cleByte);
            System.out.println("");
            System.out.println("Cle HMAC : "+cleHMAC.toString());
            System.out.println("Cle en byte : "+cleByte.toString().getBytes());
            System.out.println("Cle Cryptee(string) : "+cleCrypte.toString());
            System.out.println("Cle Cryptee(bytes) : "+cleCrypte.toString().getBytes());
            System.out.println("Cle Cryptee(bytes) : "+cleCrypte);
            
            Req.getChargeUtile().put("Cle" , cleCrypte);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex)
        {
            Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        Client.EnvoyerRequete(Req);
        Rep = Client.RecevoirReponse();
        if(Rep.getCode() == ReponseTICKMAP.SEND_SYMETRICKEY_OK)
        {
            System.out.println("ok!");
        }
    
    }
    
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