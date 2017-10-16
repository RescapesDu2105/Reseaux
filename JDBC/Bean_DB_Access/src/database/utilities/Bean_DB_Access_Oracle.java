/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.utilities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philippe
 */
public class Bean_DB_Access_Oracle extends Bean_DB_Access {

    public Bean_DB_Access_Oracle(String h, String p, String l, String pwd) {
        super(h, p, l, pwd, null);        
        setDriver("oracle.jdbc.driver.OracleDriver");
    }

    @Override
    public String Connexion() { //SQLException, ClassNotFoundException, SQLRecoverableException {
        String URL = "jdbc:oracle:thin:@localhost:" + getPort() + "/orcl";
        
        try {
            Class.forName(getDriver());
                    
            setConnection(DriverManager.getConnection(URL, getLogin(), getPassword()));
            setStatement(getConnection().createStatement());
        }
        catch (SQLException Ex)
        {
            System.out.println("Code SQL : " + Ex.getErrorCode() + " - Erreur SQL : " + Ex.getMessage());
            return Ex.getMessage();
            
        } catch (ClassNotFoundException Ex) {
            Logger.getLogger(Bean_DB_Access_Oracle.class.getName()).log(Level.SEVERE, null, Ex);
            return Ex.getMessage();
        }
        return null;
    }       
}
