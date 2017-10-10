/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author mhouse
 */
public class BucketsEvent {
	private ArrayList<EventData> eventData;
    public final EventType eventType;
	
	public BucketsEvent ( EventType t) {
		eventType = t;
		eventData = new ArrayList();
	}
	
	public EventType type () {
		return eventType;
	}
	
	public BucketsEvent add ( EventData... d ) {
		eventData.addAll(Arrays.asList(d));
		return this;		
	}
	
	public EventData get ( String k ) {
		for (EventData d : eventData) {
			if (d.key.equals(k)) {
				return d;
			}
		}
		return null;
	}
	
}
