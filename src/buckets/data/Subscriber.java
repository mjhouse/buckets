/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data;
/**
 *
 * @author mhouse
 */
public interface Subscriber<T> {
	public void notify( String name, T e );
}
