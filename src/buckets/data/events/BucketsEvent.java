/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An event passed through the Broadcaster.
 *
 * @author mhouse
 */
public class BucketsEvent {

    private ArrayList<EventData> eventData;

    /**
     * An enumerated representing an event.
     */
    public final EventType eventType;

    /**
     * Constructor that accepts a type for the event.
     *
     * @param t the type of event to create.
     */
    public BucketsEvent(EventType t) {
        eventType = t;
        eventData = new ArrayList();
    }

    /**
     * Get the type of this event.
     *
     * @return the type of the event.
     */
    public EventType type() {
        return eventType;
    }

    /**
     * Add data to a BucketsEvent.
     *
     * @param d the EventData to store.
     * @return the current BucketsEvent
     */
    public BucketsEvent add(EventData... d) {
        eventData.addAll(Arrays.asList(d));
        return this;
    }

    /**
     * Get EventData from the event.
     *
     * @param k get a value by key from the BucketsEvent.
     * @return the EventData for a given key.
     */
    public EventData get(String k) {
        for (EventData d : eventData) {
            if (d.key.equals(k)) {
                return d;
            }
        }
        return null;
    }

}
