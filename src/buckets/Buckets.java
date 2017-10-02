/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

// logging imports
import buckets.actions.Move;
import buckets.rules.Rule;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blankie
 */
public class Buckets {
    private static Logger log = Logger.getLogger("buckets");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	log.setLevel(Level.INFO);
	log.info("starting Buckets");
        
    Rule rule0 = new Rule( ".*?\\.txt", new Move("/home/mhouse/Downloads") );
	Rule rule1 = new Rule( ".*?\\.txt", new Move("/home/mhouse/Downloads") );
    System.out.println(rule0.isEqual(rule1));
        //Manager manager = new Manager();
	//manager.run();
        
        /* LOG DEMO
        // add a file handler for the log. Right now, the path is manual, but when we 
        // have persistant configuration, we'll have an option for this.
        try { log.addHandler(new FileHandler("/home/mhouse/Projects/java/buckets/data/logs/buckets.log"));}
        catch (IOException e) { log.warning(e.toString()); }
	*/
        
        /* DATABASE DEMO
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:buckets;create=true");
            conn.setAutoCommit(false);
            
            Statement s = conn.createStatement();
            s.execute("create table location(num int, addr varchar(40))");
        } catch (SQLException e) {
            System.out.println(e);
        }
        */
        
    }
    
}
