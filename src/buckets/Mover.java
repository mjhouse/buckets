/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

// imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * moves a file 
 * @author mhouse
 */
public class Mover {
    private String source;
    private String destination;
    
    
    public Mover () {
    }
    
    /**
     * 
     * @return true if the file was moved successfully, else false 
     */
    public Boolean move ( String src, String dst  ) {
	source = src;
	destination = dst;
	
	Path file = Paths.get(source);
	Path dir = Paths.get(destination, "test.txt");
	
	try {
	    Files.move(file, dir);
	    return true;
	} catch (IOException e) {
	    // file, destination don't exist or couldn't move.
	}
	
	// if we get here, the file didn't move.
	return false;
    }
    
    /* ---------------------------------------------------------------------- */
    // Setters/ Getters
    public void setSource ( String src ) {
	source = src;
    }
    
    public String getSource () {
	return source;
    }

    public void setDestination ( String dst ) {
	destination = dst;
    }
    
    public String getDestination () {
	return destination;
    }
    
}
