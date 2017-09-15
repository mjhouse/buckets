/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

// imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * moves a file 
 * @author mhouse
 */
public class Mover {
    public final String source;
    public final String destination;
    
    
    public Mover ( String src, String dst ) {
	source = src;
	destination = dst;
    }
    
    /**
     * 
     * @return true if the file was moved successfully, else false 
     */
    public Boolean move () {
	Path file = Paths.get(source);
	Path dir = Paths.get(destination, file.toString());
	
	/* Check that:
		The file exists and is NOT a directory 
		The dir exists and IS a directory
	*/
	try {
	    Files.move(file, dir);
	    return true;
	} catch (IOException e) {
	    // file, destination don't exist or couldn't move.
	}
	
	// if we get here, the file didn't move.
	return false;
    }
    
}
