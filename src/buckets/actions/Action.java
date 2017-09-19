/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.actions;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author mhouse
 */
public abstract class Action {
    
    public Action () {}
    
    public abstract void apply( Path p ) throws IOException;
}
