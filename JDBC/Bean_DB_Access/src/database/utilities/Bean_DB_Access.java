/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.utilities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Philippe
 */
abstract public class Bean_DB_Access implements Serializable {
    protected String Driver;
    protected String Host;
    protected String Port;
    protected String Login;
    protected String Password;
    protected String Schema;
    protected Connection Connection;
    protected Statement Statement;
    
    public Bean_DB_Access(String h, String p, String l, String pwd, String s){
        setHost(h);
        setPort(p);
        setLogin(l);
        setPassword(pwd);
        setSchema(s);
    }
    
    /* Connexion - Deconnexion */
    abstract public String Connexion();//SQLException, ClassNotFoundException, SQLRecoverableException;  
    public void Deconnexion() throws SQLException {
        if (Statement != null) {
            Statement.close();
        }
    }
        
    /* Requetes */
    public ResultSet Select(String Requete) throws SQLException{
        try
        {
            Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(Requete);
        }
        catch (SQLException Ex)
        {
            if (!Connection.getAutoCommit())
                Rollback();
        }
        
        return null;
    }    
    public synchronized int Update(String Requete) throws SQLException{
        try
        {
            return Statement.executeUpdate(Requete);
        }
        catch (SQLException Ex)
        {
            if (!Connection.getAutoCommit())
                Rollback();
        }
        
        return 0;
    }    
    
    /* Commit - Rollback */
    public void setAutoCommit(boolean AC) throws SQLException {
        try
        {
            Connection.setAutoCommit(AC);
        }
         catch (SQLException Ex)
        {
            if (!Connection.getAutoCommit())
                Rollback();
            System.out.println("Erreur SQL : " + Ex.getMessage());
        }
    }
    public void getAutoCommit() throws SQLException {
        boolean bool;
        
        try
        {
            bool = Connection.getAutoCommit();
        }
        catch (SQLException Ex)
        {
            if (!Connection.getAutoCommit())
                Rollback();
            System.out.println("Erreur SQL : " + Ex.getMessage());
        }
    }
    public void Commit() throws SQLException {
        try{
            Connection.commit();
        }
        catch (SQLException Ex) {
            if (!Connection.getAutoCommit())
                Rollback();
            System.out.println("Erreur SQL : " + Ex.getMessage());
        }
    }    
    public void Rollback() {
        try{
            Connection.rollback();
        }
        catch (SQLException Ex) {
            System.out.println("Code SQL : " + Ex.getErrorCode() + " - Erreur SQL : " + Ex.getMessage());
        }
    }
    
    
    /* Setters - Getters*/
    public String getDriver() {
        return Driver;
    }

    public void setDriver(String Driver) {
        this.Driver = Driver;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String Port) {
        this.Port = Port;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getSchema() {
        return Schema;
    }

    public void setSchema(String Schema) {
        this.Schema = Schema;
    }

    public Connection getConnection() {
        return Connection;
    }

    public void setConnection(Connection Connection) {
        this.Connection = Connection;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement = Statement;
    }
    
}
