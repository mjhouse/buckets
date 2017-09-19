/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.rules;

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
    private Action action;
    
    /**
     * construct a rule with a regex pattern and an action
     * @param r the regular expression pattern to match with
     * @param a the action to apply to the matched path
     */
    public Rule ( String r, Action a ) {
        pattern = Pattern.compile(r);
        action = a;
    }
    
    /**
     * construct a rule with only a pattern
     * @param r the regular expression patter to match with
     */
    public Rule ( String r ) {
	pattern = Pattern.compile(r);
	action = null;
    }
    
    /**
     * if the regex pattern matches the given path (p)
     * then the rule's action will be applied.
     * 
     * @param p the path to match 
     * @throws IOException 
     * @return bool indicating match
     */
    public Boolean apply ( Path p ) throws IOException {
        Matcher matcher = pattern.matcher(p.toString());
	Boolean match = matcher.matches(); 
        if (match && action!=null) action.apply( p );
	return match;
    }
    
    /**
     * get the currently set action for this rule
     * @return the current action for this rule 
     */
    public Action getAction () {
	return action;
    }
    
    /**
     * set a new action for this rule
     * @param a the new action to apply
     */
    public void setAction ( Action a ) {
	action = a;
    }
}
