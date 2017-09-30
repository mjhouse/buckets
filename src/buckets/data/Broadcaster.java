/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mhouse
 */
public class Broadcaster {
	private HashMap<String,ArrayList<Subscriber>> channels = new HashMap();
	
	public Broadcaster (String... chs) {
		for ( String ch : chs ) {
			channels.put(ch,new ArrayList());
		}
	}
	
	/**
	 * broadcast an object to all subscribers who are 
	 * capable of receiving it.
	 * @param ch the channel name
	 * @param e the object
	 */
	public <T> void broadcast ( String ch, T e) {
		ArrayList<Subscriber> subscribers = channels.get(ch);
		if (subscribers != null) {
			for (Subscriber s : subscribers) {
				try {
					s.notify(ch,e);
				} catch (ClassCastException c) {}
			}
		}
	}
	
	public void subscribe ( String channel, Subscriber subs ) {
		ArrayList<Subscriber> subscribers = channels.get(channel);
		if (subscribers != null) {
			subscribers.add(subs);
		}
	}
}
