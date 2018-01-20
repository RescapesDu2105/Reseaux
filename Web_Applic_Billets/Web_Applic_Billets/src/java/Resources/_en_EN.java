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
public class _en_EN extends ListResourceBundle
{
    static final Object[][] contents =
    {
        {"langue", "Language"},
        {"user", "Username"},
        {"userI", "Enter the username"},
        {"pwd", "Password"},
        {"pwdI", "Enter the password"},
        {"code", ""},
        {"new", "I'm new"},
        {"connexion", "Log in"},
        {"nom", "Name"},
        {"nomI", "Enter your name"},
        {"prenom", "Surname"},
        {"prenomI", "Enter your surname"},
        {"mail", "Mail address"},
        {"mailI", "Enter the address mail"},
        {"brand", "Virtual caddy of InPrES Airport"},
        {"fr_FR", "French"},
        {"en_EN", "English"},
        {"nl_NL", "Dutch"},
        {"panier", "Cart"},
        {"deconnexion", "Log out"},
        {"numvol", "Flight number"},
        {"nomcomp", "Airline name"},
        {"dest", "Destination"},
        {"heuredep", "Departure time"},
        {"heurearr", "Arriving time"},
        {"placesrest", "Remaining places"},
        {"nbacc", "Number of accompany persons"},
    };
    
    @Override
    protected Object[][] getContents()
    {
        return contents;
    }
    
}
