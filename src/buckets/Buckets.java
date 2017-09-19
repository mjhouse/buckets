/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import java.io.IOException;
import java.nio.file.Paths;

import buckets.actions.Action;
import buckets.actions.Move;

/**
 *
 * @author blankie
 */
public class Buckets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Action a = new Move(Paths.get("/home/mhouse/Videos"));
            a.apply(Paths.get("/home/mhouse/test.txt"));   
        } catch (IOException e) {
            
        }
    }
    
}
