/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

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

/**
 * watches a given collection of directories and notifies
 * the rule manager when files are created.
 * 
 * @author mhouse
 */
public class Watcher {
    private ArrayList<Path> directories;
    private WatchService watcher;
    
    public Watcher ( String...dirs ) {
	directories = new ArrayList();
	
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
     * Begins watching the target directories, and passes 
     * new file events to the rule manager.
     */
    public void run () {
	WatchKey key;
	while (true) {
            // poll for events from watcher
	    if ((key = watcher.poll()) == null) continue;
            
            // if an event is found, process it
	    for (WatchEvent<?> event : key.pollEvents()) {
		// get the environment from the event
		WatchEvent<Path> ev = (WatchEvent<Path>)event;

                // get the directory the event occurs in
		Path dir = (Path)key.watchable();
                
                // build an absolute path with the directory and 
                // file name.
		Path abspath = dir.resolve(ev.context());

                // do something with the absolute path.
		System.out.println(abspath);
	    }
	    Boolean valid = key.reset();
	    if(!valid) break;
	}
    }
    
    /* ---------------------------------------------------------------------- */
    /* Getters/Setters */
    
    /**
     * 
     * @return the directories being watched 
     */
    public ArrayList<Path> getWatched () {
        return this.directories;
    }
    
    /**
     * 
     * @param p paths to begin watching 
     */
    public void setWatched ( ArrayList<Path> p ) {
        this.directories = p;
    }
    
    /**
     * 
     * @return the watchservice being used
     */
    public WatchService getWatcher () {
        return this.watcher;
    }
    
    /**
     * 
     * @param w set a new watcher 
     */
    public void setWatcher ( WatchService w ) {
        this.watcher = w;
    }
    
}
