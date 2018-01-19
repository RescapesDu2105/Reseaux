/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolePAYP;

import java.io.Serializable;
import java.util.HashMap;
import requetepoolthreads.Reponse;

/**
 *
 * @author Doublon
 */
public class ReponsePAYP implements Reponse, Serializable
{
    private int Code;
    private HashMap<String, Object> ChargeUtile;
    
    public ReponsePAYP(int Code, HashMap ChargeUtile) 
    {
        setCode(Code);
        setChargeUtile(ChargeUtile);
    }
    
    public ReponsePAYP(int Code) 
    {
        setCode(Code);
        setChargeUtile(new HashMap<>());
    }
    
    @Override
    public int getCode() 
    {
        return this.Code;
    } 
    
    @Override
    public HashMap getChargeUtile() 
    {
        return this.ChargeUtile;
    }
    
    public void setChargeUtile(HashMap ChargeUtile) 
    {
        this.ChargeUtile = ChargeUtile;
    }
    
    @Override
    public void setCode(int Code) 
    {
        this.Code = Code;
    }
}
