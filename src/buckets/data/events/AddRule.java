/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

/**
 *
 * @author mhouse
 */
public class AddRule extends BucketsEvent {
    private String regex;
    private String path;
    
    public AddRule ( String r, String p ) {
        regex = r;
        path = p;
    }
    
    public String getRegex(){
        return regex;
    }
    
    public String getPath(){
        return path;
    }
}
