/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;
/**
* @author Philippe Dimartino
*/
import interfaces.SourceTaches;
import java.util.*;
public class ListeTaches implements SourceTaches
{
    private LinkedList listeTaches;
    public ListeTaches()
    {
        listeTaches = new LinkedList();
    }
    
    public synchronized Runnable getTache() throws InterruptedException
    {
        System.out.println("getTache avant wait");
        while (!existeTaches()) wait();
        return (Runnable)listeTaches.remove();
    }
    
    public synchronized boolean existeTaches()
    {
        return !listeTaches.isEmpty();
    }
    public synchronized void recordTache (Runnable r)
    {
        listeTaches.addLast(r);
        System.out.println("ListeTaches : tache dans la file");
        notify();
    }
}
