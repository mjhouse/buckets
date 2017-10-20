/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;

import javax.persistence.*;
import java.util.*;
import java.util.ArrayList;

import buckets.rules.Rule;
import buckets.rules.RuleSet;

import buckets.Watcher;

/**
 *
 * @author mhouse
 */
public class DBManager {
    private EntityManagerFactory emf;
    private EntityManager manager;
    private String location;
    
    public DBManager ( String db ) {
        location = db;
        emf = Persistence.createEntityManagerFactory(location);
        manager = emf.createEntityManager();
    }
    
    public void clear () {
        manager.getTransaction().begin();
        manager.createQuery("DELETE FROM Rule").executeUpdate();
        manager.getTransaction().commit();
    }
    
    public RuleSet loadRules () {
        TypedQuery<Rule> query = manager.createQuery("SELECT r FROM Rule r", Rule.class);
        ArrayList<Rule> results = new ArrayList<Rule>(query.getResultList());
        return new RuleSet(results);
    }

    public Watcher loadWatcher () {
        TypedQuery<Watcher> query = manager.createQuery("SELECT w FROM Watcher w", Watcher.class);
        List<Watcher> w = query.getResultList();
        
        try {
            return w.get(0);
        } catch (IndexOutOfBoundsException e){
            return new Watcher();
        } 
    }
    
    public void save ( RuleSet rs ) {
        ArrayList<Rule> rules = rs.get();
        manager.getTransaction().begin();
        for (Rule r : rules) {
            manager.persist(r);
        }
        manager.getTransaction().commit();
    }

    public void save ( Watcher w ) {
        manager.getTransaction().begin();
        manager.persist(w);
        manager.getTransaction().commit();
    }
    
    public void exit () {
        manager.close();
        emf.close();
    }

}
