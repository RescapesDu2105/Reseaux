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
    public final static int STATUS_OK = 200;    
    public final static int WRONG_USER_PASSWORD = 401;
    public final static int INTERNAL_SERVER_ERROR = 500;
    
    public final static String WRONG_USER_PASSWORD_MESSAGE = "Nom d'utilisateur ou mot de passe incorrect";
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
    private void setCodeRetour(int CodeRetour) {
        this.CodeRetour = CodeRetour;
    }
    
}
