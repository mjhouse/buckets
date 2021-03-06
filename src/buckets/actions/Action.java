/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.actions;

// system imports
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import javax.persistence.Embeddable;

/**
 * the abstract base class for actions.
 *
 * @author mhouse
 */
@Embeddable
public abstract class Action implements Serializable {

    /**
     * constructor for abstract class
     */
    public Action() {
    }

    /**
     * function will be overridden by extended classes
     *
     * @param p single path to apply action to
     * @throws IOException
     */
    public abstract void apply(Path p) throws IOException;

    /**
     * Compare this Action to another.
     *
     * @param a an Action to compare too
     * @return whether the Action is the same or not.
     */
    public abstract Boolean equals(Action a);
}
