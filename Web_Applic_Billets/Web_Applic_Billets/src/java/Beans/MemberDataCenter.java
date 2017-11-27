package Beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Classes.Commandes;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 *
 * @author Philippe
 */
public class MemberDataCenter implements Serializable 
{
    private int IdClient;
    private String Nom;
    private String Prenom;
    private ArrayList<Commandes> Panier;

    public MemberDataCenter() 
    {
        this.Nom = null;
        this.Prenom = null;
        this.Panier = new ArrayList<>();
    }
    
    public MemberDataCenter(int IdClient, String Nom, String Prenom) 
    {
        this.IdClient = IdClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Panier = new ArrayList<>();
    }   
    
    public void TrierPanier()
    {
        Collections.sort(getPanier(), (Commandes Commande1, Commandes Commande2) -> 
        {
            int Comparison = Commande1.getDateCommande().compareTo(Commande2.getDateCommande());
            
            if (Comparison == 0)
            {
                return Integer.compare(Commande1.getNumeroCommande(), Commande2.getNumeroCommande());
            }
            else
                return Comparison;
        });
    }
    
    public boolean equals(MemberDataCenter MDC)
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

    public ArrayList<Commandes> getPanier() {
        return Panier;
    }

    public void setPanier(ArrayList<Commandes> Panier) {
        this.Panier = Panier;
    }

    public int getIdClient() {
        return IdClient;
    }

    public void setIdClient(int IdClient) {
        this.IdClient = IdClient;
    }
}
