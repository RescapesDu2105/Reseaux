package Classes;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Locale;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Philippe
 */
public class Commandes 
{
    private int NumeroCommande;
    private Timestamp DateCommande;
    private int NumeroVol;
    private String NomCompagnie;
    private String Destination;
    private Timestamp DateDepart;
    private Timestamp DateArrivee;
    private int NbAccompagnants;

    public Commandes(int NumeroCommande, Timestamp DateCommande, int NumeroVol, String NomCompagnie, String Destination, Timestamp DateDepart, Timestamp DateArivee, int NbAccompagnants) 
    {
        this.NumeroCommande = NumeroCommande;
        this.DateCommande = DateCommande;
        this.NumeroVol = NumeroVol;
        this.NomCompagnie = NomCompagnie;
        this.Destination = Destination;
        this.DateDepart = DateDepart;
        this.DateArrivee = DateArivee;
        this.NbAccompagnants = NbAccompagnants;            
    }

    public String getHeureDepart() 
    {
        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(getDateDepart());
    }

    public String getHeureArrivee() 
    {
        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(getDateArrivee());
    }


    public int getNumeroCommande() {
        return NumeroCommande;
    }

    public void setNumeroCommande(int NumeroCommande) {
        this.NumeroCommande = NumeroCommande;
    }

    public Timestamp getDateCommande() {
        return DateCommande;
    }

    public void setDateCommande(Timestamp DateCommande) {
        this.DateCommande = DateCommande;
    }

    public int getNumeroVol() {
        return NumeroVol;
    }

    public void setNumeroVol(int NumeroVol) {
        this.NumeroVol = NumeroVol;
    }

    public String getNomCompagnie() {
        return NomCompagnie;
    }

    public void setNomCompagnie(String NomCompagnie) {
        this.NomCompagnie = NomCompagnie;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String Destination) {
        this.Destination = Destination;
    }

    public Timestamp getDateDepart() {
        return DateDepart;
    }

    public void setDateDepart(Timestamp DateDepart) {
        this.DateDepart = DateDepart;
    }

    public Timestamp getDateArrivee() {
        return DateArrivee;
    }

    public void setDateArrivee(Timestamp DateArrivee) {
        this.DateArrivee = DateArrivee;
    }

    public int getNbAccompagnants() {
        return NbAccompagnants;
    }

    public void setNbAccompagnants(int NbAccompagnants) {
        this.NbAccompagnants = NbAccompagnants;
    }
}
