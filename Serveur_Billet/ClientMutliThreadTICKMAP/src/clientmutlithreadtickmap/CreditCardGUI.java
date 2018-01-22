/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmutlithreadtickmap;

import cryptographie.CleSecrete;
import cryptographie.ClientBD;
import cryptographie.CryptageAsymetrique;
import cryptographie.CryptageSymetrique;
import cryptographie.KeyStoreUtils;
import cryptographie.PayementInfo;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt.ENCRYPT_MODE;
import protocolePAYP.ReponsePAYP;
import protocolePAYP.RequetePAYP;
import static protocolePAYP.RequetePAYP.REQUEST_SEND_PAYMENT;
import protocoleTICKMAP.ReponseTICKMAP;
import protocoleTICKMAP.RequeteTICKMAP;
import static protocoleTICKMAP.RequeteTICKMAP.REQUEST_PAYMENT_ACCEPTED;

/**
 *
 * @author Doublon
 */
public class CreditCardGUI extends javax.swing.JFrame
{
    private final static String keyStorePath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ClientKeystore.jks";
    private final static String keyStoreDirPath = System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator");
    private final static String keyStorePsw = "123Soleil";
    private final static String aliasKeyStrore = "clientprivatekey";
    private final static String aliasCertifServPayment = "certifservpayment";
    private static String keySecretClient = "SecretKeyClient.ser";
    
    private int montant ;
    private Client client;
    private ClientBD clientBD;
    private KeyStoreUtils ks;
    private X509Certificate certifServeur;
    private String creditCard;
    /**
     * Creates new form CreditCardGUI
     */
    public CreditCardGUI(Client cli ,int mont , ClientBD clibd)
    {
        setClient(cli);
        setClientBD(clibd);
        setMontant(mont);
        initComponents();
        setLocationRelativeTo(null); 
        System.out.println("Montant : "+getMontant());
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

        jButtonPayment = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();
        jLabelCreditCard = new javax.swing.JLabel();
        jTextFieldCreditCard = new javax.swing.JTextField();
        jLabelFeedback = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonPayment.setText("Confirmer");
        jButtonPayment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonPaymentActionPerformed(evt);
            }
        });

        jButtonAnnuler.setText("Annuler");
        jButtonAnnuler.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonAnnulerActionPerformed(evt);
            }
        });

        jLabelCreditCard.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelCreditCard.setText("Carte de crédit du client :");

        jLabelFeedback.setHorizontalAlignment(JLabel.CENTER);
        jLabelFeedback.setVerticalAlignment(JLabel.CENTER);
        jLabelFeedback.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jButtonPayment)
                .addGap(79, 79, 79)
                .addComponent(jButtonAnnuler)
                .addContainerGap(88, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCreditCard, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(jTextFieldCreditCard)
                    .addComponent(jLabelFeedback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(105, 105, 105))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabelCreditCard)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldCreditCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabelFeedback, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPayment)
                    .addComponent(jButtonAnnuler))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAnnulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAnnulerActionPerformed
    {//GEN-HEADEREND:event_jButtonAnnulerActionPerformed
        getClient().Deconnexion();
        this.dispose();
    }//GEN-LAST:event_jButtonAnnulerActionPerformed

    private void jButtonPaymentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonPaymentActionPerformed
    {//GEN-HEADEREND:event_jButtonPaymentActionPerformed
        try
        {
            if(jTextFieldCreditCard.getText().isEmpty())
                JOptionPane.showMessageDialog(this, "Le champ est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
            else
            {
                /******************************HANDSHAKE*****************************/
                setCreditCard(jTextFieldCreditCard.getText());
                getClient().ConnexionPAYP();
                PayementInfo payement = new PayementInfo(getCreditCard(),getClientBD().getNom(),getMontant());

                RequestSendCertificate(keyStorePath,keyStorePsw,aliasKeyStrore);
                boolean ok = RequestSendPayment(payement);
                jButtonPayment.setEnabled(false);
                jButtonAnnuler.setEnabled(false);

                if(ok)
                {
                    jLabelFeedback.setForeground(Color.GREEN);
                    jLabelFeedback.setText("Paiement validé !");                
                }
                else
                {
                    jLabelFeedback.setForeground(Color.red);
                    jLabelFeedback.setText("Paiement refusé !");                
                }
                RequestPaymentAccepted(ok,getClientBD(),payement);
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonPaymentActionPerformed

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
            java.util.logging.Logger.getLogger(CreditCardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(CreditCardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(CreditCardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(CreditCardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new CreditCardGUI(null,0,null).setVisible(true);
            }
        });
    }

    public int getMontant()
    {
        return montant;
    }

    public void setMontant(int montant)
    {
        this.montant = montant;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public ClientBD getClientBD()
    {
        return clientBD;
    }

    public void setClientBD(ClientBD clientBD)
    {
        this.clientBD = clientBD;
    }

    public String getCreditCard()
    {
        return creditCard;
    }

    public void setCreditCard(String creditCard)
    {
        this.creditCard = creditCard;
    }
    
    public void RequestSendCertificate(String keystorelocation, String psw , String alias )
    {
        RequetePAYP ReqPAYP = new RequetePAYP(RequetePAYP.REQUEST_SEND_CERTIFICATE);
        ReponsePAYP RepPAYP = null;
        
        try
        {
            ks=new KeyStoreUtils(keystorelocation,psw,alias);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException ex)
        {
            Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ReqPAYP.getChargeUtile().put("Certificate" , ks.getCertif());
        getClient().EnvoyerRequete(ReqPAYP);

        RepPAYP = getClient().RecevoirReponsePAYP();
        if(RepPAYP.getCode() == ReponsePAYP.REQUEST_SEND_CERTIFICATE_OK)
        {
            try {
                certifServeur=(X509Certificate) RepPAYP.getChargeUtile().get("Certificate");
                             
                ks.saveCertificate(aliasCertifServPayment, certifServeur);
                ks.SaveKeyStore(keyStorePath, keyStorePsw);
            } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
                Logger.getLogger(AuthentificationGUI.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }    
    }
    
    public boolean RequestSendPayment(PayementInfo payement)
    {
        RequetePAYP ReqPAYP = new RequetePAYP(RequetePAYP.REQUEST_SEND_PAYMENT);
        ReponsePAYP RepPAYP = null;
        
        try
        {  
            /****************************CRYPTAGE DU PAYEMENT**********************/
            System.out.println("cryptage du client...");
            Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            chiffrement.init(ENCRYPT_MODE, certifServeur.getPublicKey());
            SealedObject sealed = new SealedObject(payement, chiffrement);
            
            /****************************SIGNATURE DU PAYEMENT*********************/
            byte[] sealedByte = ObjectToByte(sealed);
            CryptageAsymetrique cryptage = new CryptageAsymetrique();
            byte[] signature = cryptage.Signe(ks.getClePrivee(), sealedByte);
                    
            ReqPAYP.getChargeUtile().put("PayementCrypte", sealed);
            ReqPAYP.getChargeUtile().put("Signature", signature);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | IOException | IllegalBlockSizeException | InvalidKeyException | SignatureException ex)
        {
            Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        getClient().EnvoyerRequete(ReqPAYP);

        RepPAYP = getClient().RecevoirReponsePAYP();
        if(RepPAYP.getCode() == ReponsePAYP.REQUEST_SEND_PAYMENT_OK)
            return (boolean) RepPAYP.getChargeUtile().get("Ok");
        return false;
    }
    
    public void RequestPaymentAccepted(boolean accepted , ClientBD clientbd , PayementInfo pay)
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_PAYMENT_ACCEPTED);
        ReponseTICKMAP Rep = null;
        
        File f = new File(keyStoreDirPath+keySecretClient);
        if(f.exists())
        {
            try
            {  
                ObjectInputStream cleFichier =new ObjectInputStream(new FileInputStream(keyStoreDirPath+keySecretClient));
                SecretKey keyLoad=(SecretKey) cleFichier.readObject();
                cleFichier.close();
                CleSecrete cleClient=new CleSecrete(keyLoad);
                
                /*********************************CRYPTAGE DES OBJETS****************************************/
                System.out.println("cryptage des objets...");
                Cipher chiffrement = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
                chiffrement.init(ENCRYPT_MODE, keyLoad);
                SealedObject clienCrypte = new SealedObject(clientbd, chiffrement);
                SealedObject paymentCrypte = new SealedObject(pay, chiffrement);
                
                Req.getChargeUtile().put("clientbd" , clienCrypte);
                Req.getChargeUtile().put("payementinfo", paymentCrypte);
                Req.getChargeUtile().put("Ok", accepted);
                
                getClient().EnvoyerRequete(Req);
                
                Rep = getClient().RecevoirReponse();
                if(Rep.getCode() == ReponseTICKMAP.REQUEST_PAYMENT_ACCEPTED_OK)
                {
                    System.out.println("Ok!");
                }
                
                
            } catch (IOException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchProviderException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex)
            {
                Logger.getLogger(CreditCardGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public byte[] ObjectToByte(Object o)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] byteArray = null;
        
        try
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.flush();
            byteArray = bos.toByteArray();
            
            bos.close();
            return byteArray;
        } catch (IOException ex)
        {
            Logger.getLogger(PaymentGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArray;
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonPayment;
    private javax.swing.JLabel jLabelCreditCard;
    private javax.swing.JLabel jLabelFeedback;
    private javax.swing.JTextField jTextFieldCreditCard;
    // End of variables declaration//GEN-END:variables
}
