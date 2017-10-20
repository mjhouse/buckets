/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.actions;

// system imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Embeddable;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * moves given paths to an output directory.
 * @author mhouse
 */
@Embeddable
public class Move extends Action implements Serializable {
    transient private static final Logger log = Logger.getLogger("buckets.actions.move");
    private String output; 
    
     /**
     * empty move constructor 
     */
    public Move () {
        output = "";
    }
    
    /**
     * construct move from string path
     * @param to directory to move to 
     */
    public Move ( String to ) {
        this.output = to;
    }
    
    /**
     * tries to move the file to the output directory
     * @param src the file or directory to move
     * @throws IOException 
     */
    @Override
    public void apply ( Path src ) throws IOException {
        // build the full path to the new location (w/ file name)
	Path nsrc = src.toAbsolutePath();
        String name = nsrc.getName(nsrc.getNameCount()-1).toString();
        Path out = Paths.get(this.output, name);
        
        // move the file
        Files.move(src,out);
    }

    public void setOutput( String p ){
        this.output = p;
    }
    
    public String getOutput(){
        return output;
    }
	
    public Boolean Equal( Action a ){
	if(a instanceof Move){
            Move b = (Move)a;
            Boolean paths = output.equals(b.getOutput());
            return paths;
	}
	return false;
    }

    @Override
    public String toString() {
	return "Move { " + "output=" + output + " }";
    }
	
}
