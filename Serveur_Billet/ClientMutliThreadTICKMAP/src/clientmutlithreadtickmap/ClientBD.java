/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmutlithreadtickmap;

/**
 *
 * @author Doublon
 */
public class ClientBD
{
    private String Nom;
    private String Prenom;
    private int nbAccompagnant;

    public ClientBD(String nom , String prenom, int nbaccompagnant)
    {
        setNom(nom);
        setPrenom(prenom);
        setNbAccompagnant(nbaccompagnant);
    }
    
    public String getNom()
    {
        return Nom;
    }

    public void setNom(String Nom)
    {
        this.Nom = Nom;
    }

    public String getPrenom()
    {
        return Prenom;
    }

    public void setPrenom(String Prenom)
    {
        this.Prenom = Prenom;
    }

    public int getNbAccompagnant()
    {
        return nbAccompagnant;
    }

    public void setNbAccompagnant(int nbAccompagnant)
    {
        this.nbAccompagnant = nbAccompagnant;
    }
    
    
    
}
