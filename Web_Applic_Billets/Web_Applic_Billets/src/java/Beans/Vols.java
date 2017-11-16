/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Classes.Vol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Philippe
 */
public class Vols implements Serializable 
{
    private ArrayList<Vol> Vols;
    
    public Vols() 
    {
        this.Vols = new ArrayList<>();
    }   
    
    public void TrierListeVols()
    {
        Collections.sort(getVols(), (Vol Vol1, Vol Vol2) -> 
        {
            int Comparison = Vol1.getHeureDepart().compareTo(Vol2.getHeureDepart());
            
            if (Comparison == 0)
            {
                return Integer.compare(Vol1.getNumeroVol(), Vol1.getNumeroVol());
            }
            else
                return Comparison;
        });
    }

    public Vol getVol(int IdVol)
    {
        boolean Trouve = false;
        Vol Vol = null;
        
        for (Iterator<Vol> it = getVols().iterator(); !Trouve && it.hasNext();) 
        {
            Vol = it.next();
            Trouve = (Vol.getIdVol() == IdVol);
        }
        
        return Vol;
    }
    
    public ArrayList<Vol> getVols() {
        return Vols;
    }

    public void setVols(ArrayList<Vol> Vols) {
        this.Vols = Vols;
    }    
}
