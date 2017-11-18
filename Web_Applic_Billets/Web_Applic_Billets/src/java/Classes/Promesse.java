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
public class Promesse implements Serializable
{
    private int IdPromesse;
    private Timestamp DatePromesse;
    private int IdVol;
    private int NbAccompagnants;

    public Promesse(int IdPromesse, Timestamp DatePromesse, int NbAccompagnants, int IdVol) 
    {
        this.IdPromesse = IdPromesse;
        this.DatePromesse = DatePromesse;
        this.NbAccompagnants = NbAccompagnants; 
        this.IdVol = IdVol;           
    }

    public Promesse()
    {
        
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(!Promesse.class.isAssignableFrom(obj.getClass()))
            return false;
        
        final Promesse other = (Promesse) obj;        
        
        return this.IdPromesse == other.IdPromesse;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.IdPromesse;
        return hash;
    }

    public int getIdPromesse() {
        return IdPromesse;
    }

    public void setIdPromesse(int IdPromesse) {
        this.IdPromesse = IdPromesse;
    }

    public String getDatePromesse(Locale locale)
    {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale).format(DatePromesse);
    }
    
    public Timestamp getDatePromesse() {
        return DatePromesse;
    }

    public void setDatePromesse(Timestamp DatePromesse) {
        this.DatePromesse = DatePromesse;
    }  
    
    public int getNbAccompagnants() {
        return NbAccompagnants;
    }

    public void setNbAccompagnants(int NbAccompagnants) {
        this.NbAccompagnants = NbAccompagnants;
    }

    public int getIdVol() {
        return IdVol;
    }

    public void setIdVol(int IdVol) {
        this.IdVol = IdVol;
    }
}
