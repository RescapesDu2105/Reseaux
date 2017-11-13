/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requetepoolthreads;

import java.util.HashMap;

/**
 *
 * @author Philippe
 */
public interface Reponse {
    public final static int INTERNAL_SERVER_ERROR = 500;
    public final static String INTERNAL_SERVER_ERROR_MESSAGE = "Erreur interne du serveur !";  
    
    public final static int LOG_OUT_OK = 201;
    public final static String LOG_OUT_OK_MESSAGE = "Client déconnecté du serveur";
    public final static int LOGIN_OK = 202;
    public final static String LOGIN_OK_MESSAGE = "Client connecté au serveur";
    public final static int LOGIN_KO = 402;    
    public final static String WRONG_USER_PASSWORD_MESSAGE = "Nom d'utilisateur ou mot de passe incorrect !";
    
    public int getCode();
    public void setCode(int Code);
    public HashMap getChargeUtile();
}
