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
public class RemoveRule extends BucketsEvent {
    private String regex;
    private String path;
    
    public RemoveRule ( String r, String p ) {
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
