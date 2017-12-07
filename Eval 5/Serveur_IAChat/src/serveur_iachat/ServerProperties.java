package serveur_iachat;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doublon
 */
public final class ServerProperties
{
    private Properties Prop = new Properties();
    //Mets ton chemin de fichier
    //Chemin DimDim :
    //Chemin TussTuss :
    private String NomFichier = System.getProperty("user.dir").split("/dist")[0] + System.getProperty("file.separator")+ "src" + System.getProperty("file.separator") + this.getClass().getPackage().getName()+ System.getProperty("file.separator") + "config.properties";
    
    
    public ServerProperties() throws FileNotFoundException, IOException 
    {       
        FileInputStream fis = null;
        
        try 
        {
            fis = new FileInputStream(getNomFichier());
            getProp().load(fis);
            fis.close();
        } 
        catch (FileNotFoundException ex) 
        {
            FileOutputStream fos;
            try 
            {
                fos = new FileOutputStream(getNomFichier());
              
                getProp().setProperty("PORT_CON", Integer.toString(30050));
                getProp().setProperty("PORT_FLY", Integer.toString(30051));
                getProp().setProperty("ADRESSEIP", "127.0.0.1"); 
                getProp().setProperty("HOST_BD", "localhost");
                getProp().setProperty("PORT_BD", "3306");   
                getProp().setProperty("SCHEMA_BD", "bd_airport");
                
                try 
                {                   
                    getProp().store(fos, null);
                } 
                catch (IOException ex1) 
                {
                    System.exit(1);
                }
            } 
            catch (FileNotFoundException ex1) 
            {
                System.exit(1);
            }
        } 
        catch (IOException ex) 
        {
            System.exit(1);
        }
    }
       
    public String getNomFichier() 
    {
        return NomFichier;
    }

    public final void setNomFichier(String NomFichier) 
    {
        this.NomFichier = NomFichier;
    }
    
    public Properties getProp() 
    {
        return Prop;
    }

    public void setProp(Properties Prop) 
    {
        this.Prop = Prop;
    }   
}
