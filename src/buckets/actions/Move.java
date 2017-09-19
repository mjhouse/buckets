/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.actions;

// local imports

// system imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * moves given paths to an output directory.
 * @author mhouse
 */
public class Move extends Action {
    private final Path outDir; 
    
    /**
     * 
     * @param to directory to move to 
     */
    public Move ( Path to ) {
        this.outDir = to;
    }
    
    /**
     * tries to move the file to the output directory
     * @param src the file or directory to move
     * @throws IOException 
     */
    @Override
    public void apply ( Path src ) throws IOException {
        // build the full path to the new location (w/ file name)
        String name = src.getName(src.getNameCount()-1).toString();
        Path out = Paths.get(this.outDir.toString(), name);
        
        // move the file
        Files.move(src,out);
    }
}
