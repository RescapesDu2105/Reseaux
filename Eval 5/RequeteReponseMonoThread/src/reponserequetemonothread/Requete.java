/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reponserequetemonothread;

import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author Doublon
 */
public interface Requete
{
    public Runnable createRunnable(Properties Prop);
    public Reponse getReponse();
    public HashMap getChargeUtile();
    public String getNomTypeRequete();
}
