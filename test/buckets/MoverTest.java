/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import java.io.File;
import java.io.IOException;
import buckets.Mover;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mhouse
 */
public class MoverTest {
    
    public MoverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of move method, of class Mover.
     */
    @Test
    public void testMove() {
	System.out.println("move");
	File src = create("test.txt","f");
	File dst = create("testDir","d");
	Mover instance = new Mover();
	
	try {
	    assert !instance.move( "NOFI.txt", dst.getCanonicalPath() ) : "succeeded with non-existant file";
	    assert !instance.move( src.getCanonicalPath(), "NODR" ) : "succeeded with non-existant directory";
	    assert instance.move( src.getCanonicalPath(), dst.getCanonicalPath() ) : "failed to move test file";
	} catch (IOException e) {
	    
	}
    }
    
    public File create ( String n, String t ) {
	File s = new File(n);
	try {
	    if ( t == "d")
		s.mkdir();
	    else 
		s.createNewFile();
	} catch (IOException e) {
	    assert false : "couldn't create the test files and directories" ;
	}
	return s;
    }
}
