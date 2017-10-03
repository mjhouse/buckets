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
public class EventData<T> {
	public final String key;
	public final T value;
	
	public EventData ( String k, T v ) {
		key = k;
		value = v;
	}
	
	public String asString () {
		return (String)value;
	}
}
