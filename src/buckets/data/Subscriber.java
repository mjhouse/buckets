/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;
import buckets.data.events.BucketsEvent;
/**
 *
 * @author mhouse
 */
public interface Subscriber {
	public void notify( BucketsEvent e );
}
