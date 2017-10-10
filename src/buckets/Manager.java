/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.Subscriber;
import buckets.data.Broadcaster;

import buckets.data.events.*;
import buckets.ui.BucketsUI;
import java.awt.EventQueue;

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
                
        ui.initCustom();
        watcher.start();
	}
	
	public void run () {
        broadcaster.subscribe( watcher, ui, this );
		EventQueue.invokeLater(() -> { this.ui.setVisible(true); });
		
		this.broadcaster.broadcast(new BucketsEvent(EventType.INIT_ALL));
	}
	
	@Override
	public void notify ( BucketsEvent e ) {
		switch (e.type()) {
			case DIRECTORY_ADD:
			case DIRECTORY_DEL:
				ui.setDirectories(watcher.getWatched());
				break;
			case RULE_ADD:
			case RULE_DEL:
				ui.setRules(watcher.getRules());
				break;
			case INIT_ALL:
                ui.setDirectories(watcher.getWatched());
                ui.setRules(watcher.getRules());
				break;
		}
	}
}
