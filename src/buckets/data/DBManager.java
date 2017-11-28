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

    /**
     * Constructor inits a database.
     *
     * @param db path to the database.
     */
    public DBManager(String db) {
        location = db;
        emf = Persistence.createEntityManagerFactory(location);
        manager = emf.createEntityManager();
    }

    /**
     * Fetches the rules from the database.
     *
     * @return a RuleSet of found Rules.
     */
    public RuleSet loadRules() {
        TypedQuery<Rule> query = manager.createQuery("SELECT r FROM Rule r", Rule.class);
        ArrayList<Rule> results = new ArrayList<Rule>(query.getResultList());
        return new RuleSet(results);
    }

    /**
     * Removes a rule from the database by id.
     *
     * @param id the id of the Rule to delete.
     */
    public void removeRule(Long id) {
        Rule r = manager.find(Rule.class, id);
        if (r != null) {
            manager.getTransaction().begin();
            manager.remove(r);
            manager.getTransaction().commit();
        }
    }

    /**
     * Load the Watcher from the database or create a new one.
     *
     * @return a loaded Watcher.
     */
    public Watcher loadWatcher() {
        TypedQuery<Watcher> query = manager.createQuery("SELECT w FROM Watcher w", Watcher.class);
        List<Watcher> w = query.getResultList();

        try {
            return w.get(0);
        } catch (IndexOutOfBoundsException e) {
            return new Watcher();
        }
    }

    /**
     * Save the Rules from a RuleSet into the database.
     *
     * @param rs the RuleSet to save.
     */
    public void save(RuleSet rs) {
        ArrayList<Rule> rules = rs.get();
        manager.getTransaction().begin();
        for (Rule r : rules) {
            manager.persist(r);
        }
        manager.getTransaction().commit();
    }

    /**
     * Save a Watcher object in the database.
     *
     * @param w the Watcher to save.
     */
    public void save(Watcher w) {
        manager.getTransaction().begin();
        manager.persist(w);
        manager.getTransaction().commit();
    }

    /**
     * Close the open database handle.
     */
    public void exit() {
        manager.close();
        emf.close();
    }

}
