/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.actions.Action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

/**
 * matches against a path, and does some action to 
 * the matched file.
 * 
 * @author mhouse
 */
public class Rule {
    private final Pattern pattern;
    private final Action action;
    
    public Rule ( String r, Action a ) {
        pattern = Pattern.compile(r);
        action = a;
    }
    
    public Boolean apply ( Path p ) {
        Matcher matcher = pattern.matcher(p.toString());
        Boolean match = matcher.matches();
        if (match)
            this.action.apply( p );
        return match;
    }
}
