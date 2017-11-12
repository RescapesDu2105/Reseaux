package Beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class MemberDataCenter implements Serializable {
    
    private String Nom;
    private String MotDePasse;

    public MemberDataCenter() 
    {
        this.Nom = null;
        this.MotDePasse = null;
    }
    
    public MemberDataCenter(String Nom, String MotDePasse) 
    {
        this.Nom = Nom;
        this.MotDePasse = MotDePasse;
    }   
    
    public boolean equals(MemberDataCenter MDC)
    {
        return (getNom().equals(MDC.getNom()) && getMotDePasse().equals(MDC.getMotDePasse()));
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getMotDePasse() {
        return MotDePasse;
    }

    public void setMotDePasse(String MotDePasse) {
        this.MotDePasse = MotDePasse;
    }
}
