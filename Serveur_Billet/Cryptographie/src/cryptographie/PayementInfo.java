/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptographie;

import java.io.Serializable;

/**
 *
 * @author Doublon
 */
public class PayementInfo implements Serializable
{
    private String creditCard;
    private String nom;
    private int montant;

    public PayementInfo(String card , String nom , int montant)
    {
        setCreditCard(card);
        setNom(nom);
        setMontant(montant);
    }
    
    public String getCreditCard()
    {
        return creditCard;
    }

    public void setCreditCard(String creditCard)
    {
        this.creditCard = creditCard;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public int getMontant()
    {
        return montant;
    }

    public void setMontant(int montant)
    {
        this.montant = montant;
    }
    
    
}
