/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import abstract_classes.AReponse;
import java.io.Serializable;
import java.util.HashMap;
import interfaces.Reponse;

/**
 *
 * @author Philippe
 */
public class ReponseLUGAP extends AReponse {
    
    public final static int FLIGHTS_LOADED = 203;
    public final static int LUGAGES_LOADED = 204;
    public final static int LUGAGES_SAVED = 205;
    
    public final static String FLIGHTS_LOADED_MESSAGE = "Informations sur les vols envoyés";
    public final static String LUGAGES_LOADED_MESSAGE = "Informations sur les bagages envoyés";
    public final static String LUGAGES_SAVED_MESSAGE = "Informations sur les bagages sauvés";
    
    public ReponseLUGAP(int Code, HashMap ChargeUtile) 
    {
        super(Code, ChargeUtile);
    }
    
    public ReponseLUGAP(int Code) 
    {
        super(Code);
    }
}
