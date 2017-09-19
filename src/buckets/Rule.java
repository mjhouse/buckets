/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

// local imports
import buckets.actions.Action;

// system imports
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

/**
 * matches against a path, and applies some action to 
 * the matched file.
 * 
 * @author mhouse
 */
public class Rule {
    private final Pattern pattern;
    private final Action action;
    
    /**
     * 
     * @param r the regular expression pattern to match with
     * @param a the action to apply to the matched path
     */
    public Rule ( String r, Action a ) {
        pattern = Pattern.compile(r);
        action = a;
    }
    
    /**
     * if the regex pattern matches the given path (p)
     * then the rule's action will be applied.
     * 
     * @param p the path to match 
     * @throws IOException 
     */
    public void apply ( Path p ) throws IOException {
        Matcher matcher = pattern.matcher(p.toString());
        if (matcher.matches()) {
            this.action.apply( p );
        }
    }
}
