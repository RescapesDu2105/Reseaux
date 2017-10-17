/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Philippe
 */
public class FenApplicationServeur extends javax.swing.JFrame implements ConsoleServeur {
    private boolean Started = false;
    private int Port_CheckIN;
    private int Port_Bagages;
    private int Max_Clients;
    private Thread ts_Bagages;
    private Thread ts_CheckIN;
    /**
     * Creates new form FenApplicationServeur
     */
    public FenApplicationServeur() {
        initComponents();
        setLocationRelativeTo(null); 
        TraceEvenements("serveur#initialisation#main");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonStart = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableauEvenements = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application_Bagages - Serveur");

        jButtonStart.setText("Start");
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        TableauEvenements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Origine", "Requête", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(TableauEvenements);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(jButtonStart)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        if (!isStarted()) {     
            Properties Prop = new Properties();
            FileInputStream fis = null;
            String nomFichier = System.getProperty("user.dir").split("/dist")[0] + System.getProperty("file.separator")+ "src" + System.getProperty("file.separator") + this.getClass().getPackage().getName()+ System.getProperty("file.separator") + "config.properties";
            
            try {
                fis = new FileInputStream(nomFichier);
                Prop.load(fis);
                fis.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FenApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FenApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (fis != null) {
                setPort_Bagages(Integer.parseInt(Prop.getProperty("PORT_BAGAGES")));
                setMax_Clients(Integer.parseInt(Prop.getProperty("MAX_CLIENTS")));
                TraceEvenements("serveur#acquisition du port#main");
                setTs_Bagages(new ThreadServeur(getPort_Bagages(), getMax_Clients(), new ListeTaches(), this));
                getTs_Bagages().start();
                //setTs_CheckIN(ThreadServeur(getPort_CheckIN(), getMax_Clients(), new ListeTaches(), this));
                //getTs_CheckIN().start();
                setStarted(true);
                jButtonStart.setText("Stop");
            }
            else {
                TraceEvenements("serveur#initialisation#failed to read properties file");
            }
        }
        else {   
            getTs_Bagages().interrupt();
            //getTs_CheckIN().interrupt();
            setStarted(false);
            jButtonStart.setText("Start");
        }
    }//GEN-LAST:event_jButtonStartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FenApplicationServeur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenApplicationServeur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenApplicationServeur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenApplicationServeur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FenApplicationServeur().setVisible(true);
            }
        });
    }

    @Override
    public void TraceEvenements(String log) {
        ArrayList<String> Ligne = new ArrayList<>();
        StringTokenizer parser = new StringTokenizer(log, "#");
        
        while(parser.hasMoreTokens()){ 
            String Token = parser.nextToken();
            //System.out.println("Token : " + Token);
            if (Token != null)
                Ligne.add(Token);  
            //System.out.println("Ligne : " + Ligne.toString());
        }
        
        DefaultTableModel dtm = (DefaultTableModel) TableauEvenements.getModel();        
        dtm.insertRow(dtm.getRowCount(), Ligne.toArray());
        
        //System.out.println("RowCount : " + dtm.getRowCount());
        TableauEvenements.setModel(dtm);
    }

    public boolean isStarted() {
        return Started;
    }

    public void setStarted(boolean Started) {
        this.Started = Started;
    }

    public int getPort_CheckIN() {
        return Port_CheckIN;
    }

    public void setPort_CheckIN(int Port_CheckIN) {
        this.Port_CheckIN = Port_CheckIN;
    }

    public int getPort_Bagages() {
        return Port_Bagages;
    }

    public void setPort_Bagages(int Port_Bagages) {
        this.Port_Bagages = Port_Bagages;
    }

    public int getMax_Clients() {
        return Max_Clients;
    }

    public void setMax_Clients(int Max_Clients) {
        this.Max_Clients = Max_Clients;
    }

    public Thread getTs_Bagages() {
        return ts_Bagages;
    }

    public void setTs_Bagages(Thread ts_Bagages) {
        this.ts_Bagages = ts_Bagages;
    }

    public Thread getTs_CheckIN() {
        return ts_CheckIN;
    }

    public void setTs_CheckIN(Thread ts_CheckIN) {
        this.ts_CheckIN = ts_CheckIN;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
