/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.events.BucketsEvent;
import buckets.data.events.AddDirectory;
import buckets.data.events.RemoveDirectory;
import buckets.data.events.DirectoryAdded;
import buckets.data.events.DirectoryRemoved;
import buckets.data.events.OnLoad;
import buckets.data.events.AddRule;
import buckets.data.events.RemoveRule;

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
				this.addWatched(absp);
			}	
		} catch (IOException e) {
			
		}
                
                this.broadcaster.broadcast(new OnLoad());
    }
	
	@Override
	public void notify ( BucketsEvent e ) {
            if (e instanceof AddDirectory) {
                AddDirectory a = (AddDirectory)e;
                if(this.addWatched(a.getPath())){
                    this.broadcaster.broadcast(new DirectoryAdded());
                }
            }
            else if (e instanceof RemoveDirectory) {
                RemoveDirectory r = (RemoveDirectory)e;
                if(this.removeWatched(r.getPath())){
                    this.broadcaster.broadcast(new DirectoryRemoved());
                }
            }
            else if (e instanceof AddRule) {
                AddRule r = (AddRule)e;
                this.addRule(r.getRegex(), r.getPath());
            }
            else if (e instanceof RemoveRule) {
                RemoveRule r = (RemoveRule)e;
                this.removeRule(r.getRegex(), r.getPath());
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
    public Boolean addWatched ( Path p ) {
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
    public Boolean removeWatched ( Path p ) {
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
        System.out.println("REMOVE: " + r + " : " + p);
        return true;
    }
    
}
