/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philippe
 */
public class ThreadClient extends Thread {
    private SourceTaches TachesAFaire;
    private String Nom;
    
    private Runnable TacheEnCours;

    public ThreadClient(SourceTaches TachesAFaire, String Nom) {
        this.TachesAFaire = TachesAFaire;
        this.Nom = Nom;
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            try {
                System.out.println("Thread client avant get");
                TacheEnCours = TachesAFaire.getTache();
            } 
            catch (InterruptedException ex) {
                System.out.println("Interruption : " + ex.getMessage());
            }
            System.out.println("run de taches en cours");
            TacheEnCours.run();
        }
    }
    
    public SourceTaches getTachesAFaire() {
        return TachesAFaire;
    }

    public void setTachesAFaire(SourceTaches TachesAFaire) {
        this.TachesAFaire = TachesAFaire;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public Runnable getTacheEnCours() {
        return TacheEnCours;
    }

    public void setTacheEnCours(Runnable TacheEnCours) {
        this.TacheEnCours = TacheEnCours;
    }
   
    
}
