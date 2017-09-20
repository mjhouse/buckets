/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.rules.RuleSet;
import buckets.rules.Rule;
import buckets.actions.Move;
import buckets.actions.Action;
import buckets.actions.Move;


import java.io.IOException;
import java.nio.file.Paths;
import java.lang.Thread;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blankie
 */
public class Buckets {
    private static Logger log = Logger.getLogger("buckets");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	log.setLevel(Level.INFO);
	log.config("starting main");
	
	RuleSet rules = new RuleSet(
		new Rule( ".*txt$", new Move("/home/mhouse/Downloads/test/outside") )
	);
	
	Watcher w = new Watcher("/home/mhouse/Downloads/test/inside");
	w.setRules(rules);
	w.start();
	
	try {
	    Thread.sleep(1000*60);
	} catch (InterruptedException e) {
	    System.out.println(e);
	}
	
	w.stop();
    }
    
}
