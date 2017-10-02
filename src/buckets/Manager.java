/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.Subscriber;
import buckets.data.Broadcaster;

import buckets.data.events.BucketsEvent;
import buckets.data.events.DirectoryAdded;
import buckets.data.events.DirectoryRemoved;
import buckets.data.events.OnLoad;
import buckets.ui.BucketsUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

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
	}
	
	@Override
	public void notify ( BucketsEvent e ) {
            if (e instanceof DirectoryAdded || e instanceof DirectoryRemoved) {
                ui.setDirectories(watcher.getWatched());
            }
            else if (e instanceof OnLoad) {
                ui.setDirectories(watcher.getWatched());
                ui.setRules(watcher.getRules());
            }
        }
}
