/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.rules.RuleSet;

// data structures
import java.util.ArrayList;
import java.util.Map;

// file/path objects
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// watch events
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchService;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;

// async
import java.util.concurrent.CompletableFuture;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * watches a given collection of directories and notifies
 * the rule manager when files are created.
 * 
 * @author mhouse
 */
public class Watcher {
    private static Logger log = Logger.getLogger("buckets.watcher");
    private ArrayList<Path> directories;
    private RuleSet rules;
    private WatchService watcher;
    private Boolean running;
    
    /**
     * 
     * @param dirs directories to watch
     */
    public Watcher ( String...dirs ) {
	directories = new ArrayList();
	running = false;
	
        
	try {
	    watcher = FileSystems.getDefault().newWatchService();
	    
	    for ( String s : dirs ) {
		Path absp = Paths.get(s).toAbsolutePath();
		absp.register(watcher, ENTRY_CREATE);
		directories.add(absp);
	    }
		
	} catch (IOException e) {
	    System.err.println(e);
	}
    }
    
    /**
     * begin watching directories
     */
    public void start () {
	if (!running) {
            running = true;
            log.info("watching directories");
	    CompletableFuture.runAsync(this::run);
	}
    }
    
    /**
     * stop watching directories
     */
    public void stop () {
	running = false;
        log.info("stopping");
    }
    
    /**
     * Begins watching the target directories, and passes 
     * new file events to the RuleSet.
     */
    private void run () {
	WatchKey key;
	while (running) {
            // poll for events from watcher
	    if ((key = watcher.poll()) == null) continue;

            // if an event is found, process it
	    for (WatchEvent<?> event : key.pollEvents()) {
		log.info("event received");
		
		// get the environment from the event
		WatchEvent<Path> ev = (WatchEvent<Path>)event;

                // get the directory the event occurs in
		Path dir = (Path)key.watchable();
                
                // build an absolute path with the directory and 
                // file name.
		Path abspath = dir.resolve(ev.context());

                // do something with the absolute path.
		rules.apply(abspath);
	    }
	    
	    if(!key.reset()) {
		log.info("watchkey is bad");
		this.stop();
	    }
	}
    }
    
    /* ---------------------------------------------------------------------- */
    /* Getters/Setters */
    
    /**
     * 
     * @return the directories being watched 
     */
    public ArrayList<Path> getWatched () {
        return directories;
    }
    
    /**
     * 
     * @param p paths to begin watching 
     */
    public void setWatched ( ArrayList<Path> p ) {
        directories = p;
    }
    
    /**
     * 
     * @return the watchservice being used
     */
    public WatchService getWatcher () {
        return watcher;
    }
    
    /**
     * 
     * @param w set a new watcher 
     */
    public void setWatcher ( WatchService w ) {
        watcher = w;
    }
    
    /**
     * get the current RuleSet
     * @return the current set of rules
     */
    public RuleSet getRules () {
        return rules;
    }
    
    /**
     * use a new RuleSet
     * @param r the RuleSet to use
     */
    public void setRules ( RuleSet r ) {
        rules = r;
    }
    
}
