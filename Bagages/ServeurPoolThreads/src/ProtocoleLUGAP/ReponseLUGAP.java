/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import java.io.Serializable;
import requetepoolthreads.Reponse;

/**
 *
 * @author Philippe
 */
public class ReponseLUGAP implements Reponse, Serializable {
    public final static int LOGIN_OK = 1;
    public final static int LOGIN_KO = 2;
    //public final static int KEY_GENERATED = 3;
    
    private int CodeRetour;
    private String ChargeUtile;

    
    public ReponseLUGAP(int CodeRetour, String ChargeUtile) {
        setCodeRetour(CodeRetour);
        setChargeUtile(ChargeUtile);
    }
    
    @Override
    public int getCode() {
        return this.CodeRetour;
    }    
    public String getChargeUtile() {
        return this.ChargeUtile;
    }    
    public void setChargeUtile(String ChargeUtile) {
        this.ChargeUtile = ChargeUtile;
    }
    private void setCodeRetour(int CodeRetour) {
        this.CodeRetour = CodeRetour;
    }
    
}
