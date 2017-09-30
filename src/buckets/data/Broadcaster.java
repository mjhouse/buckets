/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;
import buckets.data.events.BucketsEvent;

import java.util.ArrayList;

/**
 *
 * @author mhouse
 */
public class Broadcaster {
	private ArrayList<Subscriber> subscribers = new ArrayList();
	
	public Broadcaster () {}
	
	/**
	 * broadcast an event to all subscribers.
	 * @param e the object
	 */
	public void broadcast (BucketsEvent e) {
		if (subscribers != null) {
                    for (Subscriber s : subscribers) {
                        s.notify(e);
                    }
		}
	}
	
	public void subscribe ( Subscriber... subs ) {
            for (Subscriber s : subs) {
                if (subscribers != null) {
                    subscribers.add(s);
                }   
            }
	}
        
	public void unsubscribe ( Subscriber sub ) {
		if (subscribers != null) {
			subscribers.remove(sub);
		}
	}
}
