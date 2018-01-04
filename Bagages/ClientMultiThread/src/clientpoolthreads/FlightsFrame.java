/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientpoolthreads;

import ProtocoleLUGAP.ReponseLUGAP;
import ProtocoleLUGAP.RequeteLUGAP;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Philippe
 */
public class FlightsFrame extends javax.swing.JFrame {
    private final FenAuthentification FenAuthentification;
    private final Client Client;
    private final ArrayList<HashMap<String, Object>> Vols = new ArrayList<>();
    
    /**
     * Creates new form MainFrame
     */

    FlightsFrame(FenAuthentification f, Client c) { 
        this.FenAuthentification = f;
        this.Client = c;
        
        this.setTitle("Bagagiste : " + this.Client.getNomUtilisateur());
        setLocationRelativeTo(null); 
        initComponents();        
        
        ChargerVols();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVols = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTableVols.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numéro Vol", "Compagnie", "Destination", "Heure de départ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableVols.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableVols.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVolsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableVols);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        String[] options = new String[] {"Oui", "Annuler"};
        int Choix = JOptionPane.showOptionDialog(null, "Êtes-vous sûr de vouloir vous déconnecter ?", "Déconnexion", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        if (Choix == 0)
        {
            String Error = getClient().Deconnexion();

            if(Error == null)
            {
                this.dispose();
                getFenAuthentification().setVisible(true);
                getFenAuthentification().getRootPane().setDefaultButton(getFenAuthentification().getjButton_Connexion());
            }
            else if (getClient().isConnectedToServer())
                JOptionPane.showMessageDialog(this, Error, "Erreur", JOptionPane.ERROR_MESSAGE);                
            else
            {  
                JOptionPane.showMessageDialog(this, "Le serveur est déconnecté !", "Serveur déconnecté", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }                    
        }
    }//GEN-LAST:event_formWindowClosing

    private void jTableVolsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVolsMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) 
        {
            this.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new LugagesFrame(getFenAuthentification(), getClient(), getVols().get(jTableVols.getSelectedRow())).setVisible(true);
                }
            });
        }      
    }//GEN-LAST:event_jTableVolsMouseClicked

    private void ChargerVols() 
    {
        RequeteLUGAP Req = new RequeteLUGAP(RequeteLUGAP.REQUEST_LOAD_FLIGHTS);
        DefaultTableModel dtm = (DefaultTableModel) jTableVols.getModel();
        
        getClient().EnvoyerRequete(Req);       
        ReponseLUGAP Rep = getClient().RecevoirReponse();
        
        if (Rep != null)
        {
            if (Rep.getCode() == ReponseLUGAP.FLIGHTS_LOADED)
            {
                HashMap<String, Object> RepVols = Rep.getChargeUtile();
                Object[] ligne = new Object[4];

                for (int Cpt = 1 ; Cpt <= RepVols.size() - 1 ; Cpt++) 
                {
                    Vols.add((HashMap<String, Object>) RepVols.get(Integer.toString(Cpt)));
                    HashMap<String, Object> Vol = Vols.get(Cpt - 1);
                    
                    ligne[0] = Vol.get("NumeroVol");
                    ligne[1] = Vol.get("NomCompagnie");
                    ligne[2] = Vol.get("Destination");
                    Timestamp DateHeureDepart = (Timestamp) Vol.get("DateHeureDepart");
                    ligne[3] = DateHeureDepart.toLocalDateTime().toLocalTime();            
                    dtm.insertRow(Cpt - 1, ligne);
                }            
            }
            else
            {
                JOptionPane.showMessageDialog(this, Rep.getChargeUtile().get("Message"), "Impossible de charger les vols !", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } 
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Le serveur s'est déconnecté !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public FenAuthentification getFenAuthentification() {
        return FenAuthentification;
    }

    public Client getClient() {
        return Client;
    }

    public ArrayList<HashMap<String, Object>> getVols() 
    {
        return Vols;
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVols;
    // End of variables declaration//GEN-END:variables
}
