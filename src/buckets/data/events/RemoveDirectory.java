/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author mhouse
 */
public class RemoveDirectory extends BucketsEvent {
    private Path path;
    
    public RemoveDirectory () {}
    public RemoveDirectory (String p) {
        path = Paths.get(p);
    }
    public Path getPath () { return path; }
    public void setPath ( Path p ) { path = p; }
}
