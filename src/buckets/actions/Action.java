/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.actions;

// system imports
import java.io.IOException;
import java.nio.file.Path;


/**
 * the abstract base class for actions.
 * @author mhouse
 */
public abstract class Action {
    
    /**
     * constructor for abstract class
     */
    public Action () {}
    
    /**
     * function will be overridden by extended classes
     * @param p single path to apply action to
     * @throws IOException 
     */
    public abstract void apply( Path p ) throws IOException;
	
	public abstract Boolean isEqual( Action a );
}
