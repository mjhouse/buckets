/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.events.*;

import buckets.data.Broadcaster;

import buckets.actions.Move;
import buckets.rules.Rule;
import buckets.rules.RuleSet;
import buckets.data.Subscriber;

// data structures
import java.util.ArrayList;

// file/path objects
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
import java.util.logging.Logger;

/**
 * watches a given collection of directories and notifies
 * the rule manager when files are created.
 * 
 * @author mhouse
 */
public class Watcher implements Subscriber {
    private static Logger log = Logger.getLogger("buckets.watcher");
    
    private final Broadcaster broadcaster;
    private ArrayList<Path> directories;
    private RuleSet rules;
    private WatchService watcher;
    private Boolean running;
    
    /**
     * 
     * @param dirs directories to watch
     */
    public Watcher ( Broadcaster b, String...dirs ) {
		broadcaster = b;
                directories = new ArrayList();
		rules = new RuleSet();
		running = false;
		
		try {
			watcher = FileSystems.getDefault().newWatchService();
			for ( String s : dirs ) {
				Path absp = Paths.get(s).toAbsolutePath();
				this.addWatched(absp.toString());
			}	
		} catch (IOException e) {
			
		}
    }
	
	@Override
	public void notify ( BucketsEvent e ) {
		EventData data, regex, path, idx;
		switch (e.type()) {
			case ADD_DIRECTORY: 
				data = e.get("path");
				if (data != null && this.addWatched(data.asString())) {
					this.broadcaster.broadcast(new BucketsEvent(EventType.DIRECTORY_ADD));
				}
				break;
			case DEL_DIRECTORY:
				data = e.get("path");
				if (data != null && this.removeWatched(data.asString())) {
					this.broadcaster.broadcast(new BucketsEvent(EventType.DIRECTORY_DEL));
				}
				break;
			case ADD_RULE:
				regex = e.get("regex");
				path = e.get("path");
				if (regex != null && path != null && this.addRule(regex.asString(),path.asString())) {
					this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_ADD));
				}
				break;
			case DEL_RULE:
				regex = e.get("regex");
				path = e.get("path");
				idx = e.get("index");
				if (regex != null && path != null && this.removeRule(regex.asString(),path.asString())) {
					this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_DEL));
				}
				else if (idx != null) {
					Boolean r = this.removeRule(idx.asInt());
					this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_DEL));
				}
				break;
                        case EXIT:
                            this.stop();
                            break;
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
                        if (directories.contains(dir)) {
                            // build an absolute path with the directory and 
                            // file name.
                            Path abspath = dir.resolve(ev.context());

                            // do something with the absolute path.
                            rules.apply(abspath);
                        } else {
                            key.cancel();
                        }
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
     * @param p path to begin watching
     */
    public Boolean addWatched ( String s ) {
		Path p = Paths.get(s);
        if(!directories.contains(p)){
            try {
                p.register(watcher, ENTRY_CREATE);
                directories.add(p);
                return true;   
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return false;
    }
	
    /**
     * 
     * @param p path to stop watching
     */
    public Boolean removeWatched ( String s ) {
		Path p = Paths.get(s);
        if(directories.contains(p)){
            directories.remove(p);
            return true;
        }
        return false;
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
    
    public Boolean addRule ( String r, String p ) {
        Rule rule = new Rule( r, new Move(p) );
		return rules.add(rule);
    }
    
    public Boolean removeRule ( String r, String p ) {
        Rule rule = new Rule( r, new Move(p) );
		return rules.remove(rule);
    }
	
    public Boolean removeRule ( Integer idx ) {
		return rules.remove(idx);
    }
    
}
