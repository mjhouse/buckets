/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;

import buckets.data.events.BucketsEvent;

/**
 * This interface allows implementors to receive notifications.
 *
 * @author mhouse
 */
public interface Subscriber {

    /**
     * The method used to notify implementors.
     *
     * @param e the BucketsEvent to react to.
     */
    public void notify(BucketsEvent e);
}
