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
        {"user", "Nom"},
        {"code", "Code de réservation"},
        {"mail", "Entrer l'adresse mail"},
        {"connexion", "Connexion"},
        {"brand", "Caddie Virtuel de l'InPrES Airport"},
        {"fr_FR", "Français"},
        {"en_EN", "Anglais"},
        {"nl_NL", "Néerlandais"}
    };
    
    @Override
    protected Object[][] getContents()
    {
        return contents;
    }
    
}
