package Beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class MemberDataCenter implements Serializable {
    
    private String Nom;
    private String Prenom;
    private ArrayList<Commandes> Panier = new ArrayList<>();

    public MemberDataCenter() 
    {
        this.Nom = null;
        this.Prenom = null;
    }
    
    public MemberDataCenter(String Nom, String Prenom) 
    {
        this.Nom = Nom;
        this.Prenom = Prenom;
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
}
