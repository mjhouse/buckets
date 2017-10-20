/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.rules;

import java.io.Serializable;
import javax.persistence.*;

// local imports
import buckets.actions.Action;

// system imports
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

// logging imports
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * matches against a path, and applies some action to 
 * the matched file.
 * 
 * @author mhouse
 */
@Entity
public class Rule implements Serializable {
    private static Logger log = Logger.getLogger("buckets.rules.rule");
    transient private Pattern regex;
    private String pattern;
    
    @Embedded private Action action;
    
    @Id @GeneratedValue
    private long id;

    /**
     * construct an empty rule
     */
    public Rule () {
        pattern = "";
        regex = null;
        action = null;
    }

    /**
     * construct a rule with a regex regex and an action
     * @param r the regular expression regex to match with
     * @param a the action to apply to the matched path
     */
    public Rule ( String r, Action a ) {
        pattern = r;
        regex = Pattern.compile(r);
        action = a;
    }
    
    /**
     * construct a rule with only a regex
     * @param r the regular expression patter to match with
     */
    public Rule ( String r ) {
        pattern = r;
	regex = Pattern.compile(r);
	action = null;
    }
    
    public Boolean Equal ( Rule r ) {
	Boolean patterns = getRegex().pattern().equals( r.getRegex().pattern() );
	Boolean actions = action.Equal(r.getAction());
        return patterns && actions;
    }
    
    /**
     * if the regex regex matches the given path (p)
 then the rule's action will be applied.
     * 
     * @param p the path to match 
     * @throws IOException 
     * @return bool indicating match
     */
    public Boolean apply ( Path p ) throws IOException {
        Matcher matcher = regex.matcher(p.toString());
	Boolean match = matcher.matches(); 
        if (match && action!=null) {
            action.apply( p );
        }
	return match;
    }
  
    /**
     * get the regex for this rule
     * @return the compiled regex regex
     */
    public Pattern getRegex () {
        if(regex==null){ regex = Pattern.compile(pattern); }
	return regex;
    }

    /**
     * get the regex pattern for this rule
     * @return the string pattern
     */
    public String getPattern () {
	return pattern;
    }
    
    /**
     * set the regex for this rule
     */
    public void setPattern ( String p ) {
        pattern = p;
	regex = Pattern.compile(p);
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

    @Override
    public String toString() {
        return "Rule { " + "pattern=" + pattern + ", action=" + action + " }";
    }
}
