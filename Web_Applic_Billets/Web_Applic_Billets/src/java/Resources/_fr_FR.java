/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.util.ListResourceBundle;

/**
 *
 * @author Philippe
 */
public class _fr_FR extends ListResourceBundle
{
    static final Object[][] contents =
    {
        {"langue", "Langue"},
        {"user", "Nom d'utilisateur"},
        {"userI", "Entrer le nom d utilisateur"},
        {"pwd", "Mot de passe"},
        {"pwdI", "Entrer le mot de passe"},
        {"code", "Code de réservation"},
        {"new", "Je suis un nouveau client"},
        {"connexion", "Connexion"},
        {"nom", "Nom de famille"},
        {"nomI", "Entrer le nom de famille"},
        {"prenom", "Prénom"},
        {"prenomI", "Entrer le prénom"},
        {"mail", "Adresse mail"},
        {"mailI", "Entrer l adresse mail"},
        {"brand", "Caddie Virtuel de l'InPrES Airport"},
        {"fr_FR", "Français"},
        {"en_EN", "Anglais"},
        {"nl_NL", "Néerlandais"},
        {"panier", "Panier"},
        {"deconnexion", "Déconnexion"},
        {"numvol", "Numéro du vol"},
        {"nomcomp", "Nom compagnie"},
        {"dest", "Destination"},
        {"heuredep", "Heure de départ"},
        {"heurearr", "Heure d'arrivée"},
        {"placesrest", "Places restantes"},
        {"nbacc", "Nombre d'accompagnants"},
    };
    
    @Override
    protected Object[][] getContents()
    {
        return contents;
    }
    
}
