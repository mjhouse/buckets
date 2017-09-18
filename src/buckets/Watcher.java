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
    
    public void run () {
	// watch the given directories
	WatchKey key;
	while (true) {
	    if ((key = watcher.poll()) == null) continue;
	    for (WatchEvent<?> event : key.pollEvents()) {
		//process
		WatchEvent<Path> ev = (WatchEvent<Path>)event;

		Path dir = (Path)key.watchable();
		Path abspath = dir.resolve(ev.context());

		System.out.println(abspath);
	    }
	    Boolean valid = key.reset();
	    if(!valid) break;
	}
    }
    
}
