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

/**
 * moves given paths to an output directory.
 * @author mhouse
 */
public class Move extends Action {
    private static Logger log = Logger.getLogger("buckets.actions.move");
    private final Path outDir; 

    /**
     * construct move from string path
     * @param to directory to move to 
     */
    public Move ( String to ) {
        this.outDir = Paths.get(to).toAbsolutePath();
    }
    
    /**
     * construct move from Path
     * @param to directory to move to 
     */
    public Move ( Path to ) {
        this.outDir = to.toAbsolutePath();
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
        Path out = Paths.get(this.outDir.toString(), name);
        
        // move the file
        Files.move(src,out);
    }

    @Override
    public String toString() {
        return "Move{" + "outDir=" + outDir + '}';
    }

}
