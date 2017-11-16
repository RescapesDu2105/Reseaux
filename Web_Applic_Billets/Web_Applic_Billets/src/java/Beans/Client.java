package Beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Classes.Promesse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Philippe
 */
public class Client implements Serializable 
{
    private int IdClient;
    private String Nom;
    private String Prenom;
    private ArrayList<Promesse> Panier;

    public Client() 
    {
        this.Nom = null;
        this.Prenom = null;
        this.Panier = new ArrayList<>();
    }
    
    public Client(int IdClient, String Nom, String Prenom) 
    {
        this.IdClient = IdClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Panier = new ArrayList<>();
    }   
    
    public void TrierPanier()
    {
        Collections.sort(getPanier(), (Promesse Promesse1, Promesse Promesse2) -> 
        {
            int Comparison = Promesse1.getDatePromesse().compareTo(Promesse2.getDatePromesse());
            
            if (Comparison == 0)
            {
                return Integer.compare(Promesse1.getIdPromesse(), Promesse2.getIdPromesse());
            }
            else
                return Comparison;
        });
    }
    
    public boolean equals(Client MDC)
    {
        return (getNom().equals(MDC.getNom()) && getPrenom().equals(MDC.getPrenom()));
    }

    
    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public ArrayList<Promesse> getPanier() {
        return Panier;
    }

    public void setPanier(ArrayList<Promesse> Panier) {
        this.Panier = Panier;
    }

    public int getIdClient() {
        return IdClient;
    }

    public void setIdClient(int IdClient) {
        this.IdClient = IdClient;
    }
}
