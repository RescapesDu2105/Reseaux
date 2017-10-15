/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurpoolthreads;

import java.util.LinkedList;

/**
 *
 * @author Philippe
 */
public class ListeTaches implements SourceTaches {
    private LinkedList ListeTaches;

    public ListeTaches() {
        this.ListeTaches = new LinkedList();
    }

    public LinkedList getListeTaches() {
        return ListeTaches;
    }

    public void setListeTaches(LinkedList ListeTaches) {
        this.ListeTaches = ListeTaches;
    }
    

    @Override
    public synchronized Runnable getTache() throws InterruptedException 
    {
        System.out.println("getTache avant wait");
        while(!existTaches()) {
            wait();
        }
        
        return (Runnable)getListeTaches().remove();
    }

    @Override
    public synchronized boolean existTaches() {
        return !getListeTaches().isEmpty();        
    }

    @Override
    public synchronized void recordTache(Runnable r) 
    {
        getListeTaches().addLast(r);
        System.out.println("ListeTaches : tache dans la file");
        notify();
    }    
}
