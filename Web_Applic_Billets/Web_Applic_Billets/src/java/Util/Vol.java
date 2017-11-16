package Util;

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
public class Vol
{
    private int NumeroVol;
    private String NomCompagnie;
    private String Destination;
    private Timestamp DateDepart;
    private Timestamp DateArrivee;
    private int NbAccompagnants;

    public Vol() 
    {
        
    }

    public Vol(int NumeroVol, String NomCompagnie, String Destination, Timestamp DateDepart, Timestamp DateArrivee, int NbAccompagnants) 
    {
        this.NumeroVol = NumeroVol;
        this.NomCompagnie = NomCompagnie;
        this.Destination = Destination;
        this.DateDepart = DateDepart;
        this.DateArrivee = DateArrivee;
        this.NbAccompagnants = NbAccompagnants;
    }
    
    
    public int getNbAccompagnants() {
        return NbAccompagnants;
    }

    public void setNbAccompagnants(int NbAccompagnants) {
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
}
