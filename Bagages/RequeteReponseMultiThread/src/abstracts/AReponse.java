/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abstracts;

import interfaces.Reponse;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author phil3
 */
public class AReponse implements Reponse, Serializable
{
    // Erreurs + Messages
    public final static int BAD_REQUEST = 400;
    public final static int LOGIN_OUT_KO = 401;    
    public final static int LOGIN_KO = 402;    
    public final static int INTERNAL_SERVER_ERROR = 500;
    public final static String BAD_REQUEST_MESSAGE = "Requête inconnue ou malformée !";  
    public final static String LOG_OUT_KO_MESSAGE = "Erreur lors de la déconnexion !";
    public final static String WRONG_USER_PASSWORD_MESSAGE = "Nom d'utilisateur ou mot de passe incorrect !";
    public final static String INTERNAL_SERVER_ERROR_MESSAGE = "Erreur interne du serveur !";    
    // OK + Messages
    public final static int LOG_OUT_OK = 201;
    public final static int LOGIN_OK = 202;
    public final static String LOG_OUT_OK_MESSAGE = "Client déconnecté du serveur";
    public final static String LOGIN_OK_MESSAGE = "Client connecté au serveur";
    
    protected int Code;
    protected HashMap<String, Object> ChargeUtile;

    
    protected AReponse(int Code, HashMap ChargeUtile) 
    {
        setCode(Code);
        setChargeUtile(ChargeUtile);
    }
    
    protected AReponse(int Code) 
    {
        setCode(Code);
        setChargeUtile(new HashMap<>());
    }
    
    @Override
    public int getCode() {
        return this.Code;
    }    
    public HashMap getChargeUtile() {
        return this.ChargeUtile;
    }    
    public void setChargeUtile(HashMap ChargeUtile) {
        this.ChargeUtile = ChargeUtile;
    }
    public void setCode(int Code) {
        this.Code = Code;
    }
}
