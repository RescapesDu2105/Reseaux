/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Util.Vol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Philippe
 */
public class Vols implements Serializable 
{
    private ArrayList<Vol> ListeVols;
    
    public Vols() 
    {
        this.ListeVols = null;
    }
    
    public Vols(ArrayList<Vol> ListeVols)
    {
        this.ListeVols = ListeVols;
        TrierListeVols();
    }
    
    public void TrierListeVols()
    {
        Collections.sort(getListeVols(), (Vol Vol1, Vol Vol2) -> 
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

    public ArrayList<Vol> getListeVols() {
        return ListeVols;
    }

    public void setListeVols(ArrayList<Vol> ListeVols) {
        this.ListeVols = ListeVols;
    }    
}
