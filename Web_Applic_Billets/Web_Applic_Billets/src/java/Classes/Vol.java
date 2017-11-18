package Classes;

import java.io.Serializable;
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
public class Vol implements Serializable
{
    private int IdVol;
    private int NumeroVol;
    private String NomCompagnie;
    private String Destination;
    private Timestamp DateDepart;
    private Timestamp DateArrivee;
    private int PlacesRestantes;

    public Vol() 
    {
        
    }

    public Vol(int IdVol, int NumeroVol, String NomCompagnie, String Destination, Timestamp DateDepart, Timestamp DateArrivee, int PlacesRestantes) 
    {
        this.IdVol = IdVol;
        this.NumeroVol = NumeroVol;
        this.NomCompagnie = NomCompagnie;
        this.Destination = Destination;
        this.DateDepart = DateDepart;
        this.DateArrivee = DateArrivee;
        this.PlacesRestantes = PlacesRestantes;
    }

    
    public int getIdVol() {
        return IdVol;
    }

    public void setIdVol(int IdVol) {
        this.IdVol = IdVol;
    }
    
    public int getPlacesRestantes() {
        return PlacesRestantes;
    }

    public void setPlacesRestantes(int PlacesRestantes) {
        this.PlacesRestantes = PlacesRestantes;
    }
    
    public String getDateDepart(Locale locale) 
    {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale).format(getDateDepart());
    }

    public String getDateArrivee(Locale locale) 
    {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale).format(getDateArrivee());
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
