/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

import java.lang.Long;

/**
 * A wrapper to hold any one of several different data types.
 *
 * @author mhouse
 * @param <T>
 */
public class EventData<T> {

    /**
     * A key for the current EventData.
     */
    public final String key;

    /**
     * The templated value for the EventData.
     */
    public final T value;

    /**
     * The constructor.
     *
     * @param k the key
     * @param v the value
     */
    public EventData(String k, T v) {
        key = k;
        value = v;
    }

    /**
     * Try to get the value as a string.
     *
     * @return value as a String.
     */
    public String asString() {
        return (String) value;
    }

    /**
     * Try to get the value as an integer.
     *
     * @return value as Integer.
     */
    public Integer asInt() {
        return (Integer) value;
    }

    /**
     * Try to get the value as a Long.
     *
     * @return value as Long.
     */
    public Long asLong() {
        return (Long) value;
    }
}
