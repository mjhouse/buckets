/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.Subscriber;
import buckets.data.Broadcaster;

import buckets.data.events.BucketsEvent;
import buckets.ui.BucketsUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

/**
 *
 * @author mhouse
 */
public class Manager implements Subscriber {
	private BucketsUI ui;
	private Watcher watcher;
        public Broadcaster broadcaster;
	
	public Manager () {
                broadcaster = new Broadcaster();
            
		watcher = new Watcher(broadcaster);
		ui = new BucketsUI(broadcaster);
                
                broadcaster.subscribe( watcher, ui );
	}
	
	public void run () {
		EventQueue.invokeLater(() -> { this.ui.setVisible(true); });
	}
	
	@Override
	public void notify ( BucketsEvent e ) {}
}
