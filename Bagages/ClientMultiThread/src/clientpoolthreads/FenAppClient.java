package clientpoolthreads;

/*
* FenAppClient.java
*/

import java.io.*;
import java.net.*;
//import ProtocoleSUM.*;
/**
* @author Vilvens
*/
public class FenAppClient extends javax.swing.JFrame
{
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    public FenAppClient() 
    { 
        initComponents(); 
    }
    
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                new FenAppClient().setVisible(true);
            }
        });
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TFAdresseServeur = new javax.swing.JTextField();
        TFPortServeur = new javax.swing.JTextField();
        RBMail = new javax.swing.JRadioButton();
        RBKey = new javax.swing.JRadioButton();
        TFRequete = new javax.swing.JTextField();
        BEnvoyer = new javax.swing.JButton();
        BAnnuler = new javax.swing.JButton();
        BFermer = new javax.swing.JButton();
        LReponse = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TFAdresseServeur.setText("192.168.0.3");

        TFPortServeur.setText("30042");

        RBMail.setSelected(true);
        RBMail.setText("Mail");

        RBKey.setText("Clé");

        TFRequete.setText("Madani");

        BEnvoyer.setText("Envoyer");
        BEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEnvoyerActionPerformed(evt);
            }
        });

        BAnnuler.setText("Annuler");

        BFermer.setText("Fermer");

        jLabel1.setText("Adresse");

        jLabel2.setText("Port");

        jLabel3.setText("Requete");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addGap(78, 78, 78)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TFAdresseServeur, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TFPortServeur, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addComponent(RBMail)
                            .addGap(52, 52, 52)
                            .addComponent(RBKey)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LReponse, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BEnvoyer)
                                    .addComponent(jLabel3))
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(BAnnuler)
                                        .addGap(34, 34, 34)
                                        .addComponent(BFermer))
                                    .addComponent(TFRequete, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFAdresseServeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFPortServeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RBMail)
                    .addComponent(RBKey))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFRequete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BEnvoyer)
                    .addComponent(BAnnuler)
                    .addComponent(BFermer))
                .addGap(18, 18, 18)
                .addComponent(LReponse, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEnvoyerActionPerformed
        // Construction de la requête
        String chargeUtile = TFRequete.getText();
        /*RequeteSUM req = null;
        if (RBMail.isSelected())
            req = new RequeteSUM(RequeteSUM.REQUEST_E_MAIL, chargeUtile);
        else 
            req = new RequeteSUM(RequeteSUM.REQUEST_TEMPORARY_KEY, chargeUtile);*/
        // Connexion au serveur
        ois=null; 
        oos=null; 
        cliSock = null;
        String adresse = TFAdresseServeur.getText();
        int port = Integer.parseInt(TFPortServeur.getText());

        try
        {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        { 
            System.err.println("Erreur ! Host non trouvé [" + e + "]"); 
        }
        catch (IOException e)
        { 
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); 
        }
        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            //oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        { 
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); 
        }
        // Lecture de la réponse
        //ReponseSUM rep = null;
        /*try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            //rep = (ReponseSUM)ois.readObject();
            //System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());
        }
        /*catch (ClassNotFoundException e)
        { 
            System.out.println("--- erreur sur la classe = " + e.getMessage()); 
        }
        catch (IOException e)
        { 
            System.out.println("--- erreur IO = " + e.getMessage()); 
        }
        LReponse.setText(rep.getChargeUtile());*/
    }//GEN-LAST:event_BEnvoyerActionPerformed

    
         
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BAnnuler;
    private javax.swing.JButton BEnvoyer;
    private javax.swing.JButton BFermer;
    private javax.swing.JLabel LReponse;
    private javax.swing.JRadioButton RBKey;
    private javax.swing.JRadioButton RBMail;
    private javax.swing.JTextField TFAdresseServeur;
    private javax.swing.JTextField TFPortServeur;
    private javax.swing.JTextField TFRequete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
