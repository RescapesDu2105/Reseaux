/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import java.io.Serializable;
import java.util.HashMap;
import requetepoolthreads.Reponse;

/**
 *
 * @author Philippe
 */
public class ReponseLUGAP implements Reponse, Serializable {
    //public final static int STATUS_OK = 200;    
    public final static int INTERNAL_SERVER_ERROR = 500;
    
    public final static int LOG_OUT_OK = 201;
    public final static int LOGIN_OK = 202;
    public final static int LOGIN_KO = 402;
    public final static int FLIGHTS_LOADED = 203;
    public final static int LUGAGES_LOADED = 204;
    public final static int LUGAGES_SAVED = 205;
    
    public final static String LOG_OUT_OK_MESSAGE = "Client déconnecté du serveur";
    public final static String LOGIN_OK_MESSAGE = "Client connecté au serveur";
    public final static String FLIGHTS_LOADED_MESSAGE = "Informations sur les vols envoyés";
    public final static String LUGAGES_LOADED_MESSAGE = "Informations sur les bagages envoyés";
    public final static String LUGAGES_SAVED_MESSAGE = "Informations sur les bagages sauvés";
    public final static String WRONG_USER_PASSWORD_MESSAGE = "Nom d'utilisateur ou mot de passe incorrect !";
    public final static String INTERNAL_SERVER_ERROR_MESSAGE = "Erreur interne du serveur !";  
    
    private int CodeRetour;
    private HashMap<String, Object> ChargeUtile;

    
    public ReponseLUGAP(int CodeRetour, HashMap ChargeUtile) 
    {
        setCodeRetour(CodeRetour);
        setChargeUtile(ChargeUtile);
    }
    
    public ReponseLUGAP(int CodeRetour) 
    {
        setCodeRetour(CodeRetour);
        setChargeUtile(new HashMap<>());
    }
    
    @Override
    public int getCode() {
        return this.CodeRetour;
    }    
    public HashMap getChargeUtile() {
        return this.ChargeUtile;
    }    
    public void setChargeUtile(HashMap ChargeUtile) {
        this.ChargeUtile = ChargeUtile;
    }
    public void setCodeRetour(int CodeRetour) {
        this.CodeRetour = CodeRetour;
    }
    
}
