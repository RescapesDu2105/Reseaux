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
public class Promesse 
{
    private int IdPromesse;
    private Timestamp DatePromesse;
    private int IdVol;
    private int NbAccompagnants;

    public Promesse(int IdPromesse, Timestamp DatePromesse, int IdVol, int NbAccompagnants) 
    {
        this.IdPromesse = IdPromesse;
        this.DatePromesse = DatePromesse;
        this.IdVol = IdVol;
        this.NbAccompagnants = NbAccompagnants;            
    }

    public Promesse()
    {
        
    }


    public int getIdPromesse() {
        return IdPromesse;
    }

    public void setIdPromesse(int IdPromesse) {
        this.IdPromesse = IdPromesse;
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
