/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import database.utilities.Bean_DB_Access;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Philippe
 */
public class Langues implements Serializable
{
    private ArrayList<String> Langues;
    
    public Langues()
    {
        Bean_DB_Access BD_compta = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_compta");
        BD_compta.Connexion();   
        try
        {
            Langues = new ArrayList<>();
            ResultSet RS = BD_compta.Select("SELECT Code FROM Langues");
            while(RS.next())
            {
                Langues.add(RS.getString(1));
            }
            RS.close();

            System.out.println("Langues = " + Langues.size());
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }  
        finally 
        {
            BD_compta.Deconnexion();
        }   
    }

    public ArrayList<String> getLangues()
    {
        return Langues;
    }
}
