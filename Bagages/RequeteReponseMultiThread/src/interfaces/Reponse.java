/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.HashMap;

/**
 *
 * @author Philippe
 */
public interface Reponse {
    public int getCode();
    public void setCode(int Code);
    public HashMap getChargeUtile();
}
