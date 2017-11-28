/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.io.IOException;

import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author blankie
 */
public class Buckets {

    private static Logger log = Logger.getLogger("buckets");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.setLevel(Level.INFO);
        log.info("starting Buckets");

        try {
            Path dir = Paths.get("").toAbsolutePath().resolve("buckets.log");
            log.addHandler(new FileHandler(dir.toString()));
        }
        catch (IOException e) {
            log.warning(e.toString());
        }

        
        Manager manager = new Manager();
        manager.run();
    }

}
