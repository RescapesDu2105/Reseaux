/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.utilities;

/**
 *
 * @author Philippe
 */
public interface URLs_Database {
    public final static String URL_MYSQL = "jdbc:mysql://";
    public final static String URL_ORACLE = "jdbc:oracle:thin:@";
    
    public String getURL();
}
