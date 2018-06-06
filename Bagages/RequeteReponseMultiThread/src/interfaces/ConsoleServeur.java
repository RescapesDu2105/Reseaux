/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Philippe
 */
public interface ConsoleServeur {
    public final static int STOPPED = 0;
    public final static int RUNNING = 1;
    
    public void TraceEvenements(String log);
}
