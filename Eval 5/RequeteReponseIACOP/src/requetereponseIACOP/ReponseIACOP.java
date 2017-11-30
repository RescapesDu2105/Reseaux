/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requetereponseIACOP;

import java.io.Serializable;
import java.util.HashMap;
import reponserequetemonothread.Reponse;

/**
 *
 * @author Doublon
 */
public class ReponseIACOP implements Reponse, Serializable
{
    private int Code;
    private HashMap<String, Object> ChargeUtile;

    public ReponseIACOP(int Code, HashMap<String, Object> ChargeUtile)
    {
        setCode(Code);
        setChargeUtile(ChargeUtile);
    }

    public ReponseIACOP(int Code)
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
