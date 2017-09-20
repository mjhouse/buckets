/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.rules;

// system imports
import java.util.ArrayList;
import java.util.Iterator;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Arrays;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhouse
 */
public class RuleSet implements Iterable<Rule> {
    private static Logger log = Logger.getLogger("buckets.rules.ruleset");
    private ArrayList<Rule> rules;
    
    /**
     * construct a RuleCollection from ArrayList
     * @param nr an Arraylist of Rule objects.
     */
    public RuleSet ( ArrayList<Rule> nr ) {
	rules = nr;
    }
    
    /**
     * construct a RuleCollection from a number of Rule objects.
     * @param nr any number of initial Rule objects 
     */
    public RuleSet ( Rule... nr ) {
	rules = new ArrayList();
	rules.addAll(Arrays.asList(nr));
    }
    
    /**
     * set the collection of rules
     * @param nr an ArrayList of Rule objects.
     */
    public void set ( ArrayList<Rule> nr ) {
	rules = nr;
    }
    
    /**
     * get the current set of Rules.
     * @return the collection of rules as an ArrayList
     */
    public ArrayList<Rule> get () {
	return rules;
    }
    
    /**
     * add a rule to the collection.
     * @param r a new Rule
     */
    public void add ( Rule r ) {
	rules.add(r);
    }
    
    /**
     * remove a Rule by index
     * @param i index of the Rule to remove.
     */
    public void remove ( int i ) {
	rules.remove(i);
    }
    
    /**
     * apply each Rule in the collection until one matches.
     * @param p the path to apply Rules to
     */
    public void apply ( Path p ) {
	for ( Rule r : this.rules ) {
	    try {
		if (r.apply(p)) return;
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
    }
    
    /**
     * implementation for iterable interface
     * @return iterator over rules 
     */
    @Override
    public Iterator<Rule> iterator () {
	return rules.iterator();
    }
}
