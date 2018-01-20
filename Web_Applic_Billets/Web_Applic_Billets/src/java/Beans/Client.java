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
    private String Mail;
    private ArrayList<Promesse> Panier;

    public Client() 
    {
        this.Nom = null;
        this.Prenom = null;
        this.Mail = null;
        this.Panier = new ArrayList<>();
    }
    
    public Client(int IdClient, String Nom, String Prenom, String Mail) 
    {
        this.IdClient = IdClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Mail = Mail;
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
    
    public boolean equals(Client Client)
    {
        return (getNom().equals(Client.getNom()) && getPrenom().equals(Client.getPrenom()));
    }
    
    public boolean ContientPromesse(int IdPromesse)
    {
        boolean contient = false;
        
        for(int i = 0 ; i < getPanier().size() && !contient ; i++)
        {
            if (getPanier().get(i).getIdPromesse() == IdPromesse)
                contient = true;
        }
        
        return contient;
    }

    public boolean RetirerPromesse(int IdPromesse)
    {
        boolean retire = false;
        
        for(int i = 0 ; i < getPanier().size() && !retire ; i++)
        {
            if (getPanier().get(i).getIdPromesse() == IdPromesse)
            {
                getPanier().remove(i);
                retire = true;
            }
        }
        
        return retire;
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

    public String getMail()
    {
        return Mail;
    }

    public void setMail(String Mail)
    {
        this.Mail = Mail;
    }
}
