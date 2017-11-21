/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.Subscriber;
import buckets.data.Broadcaster;
import buckets.data.DBManager;

import buckets.rules.Rule;
import buckets.rules.RuleSet;

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
        private DBManager database;
        public Broadcaster broadcaster;
        
	public Manager () {
            // set up a shutdown hook for clean up
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() { onShutdown(); }
            });
            
            
            
            broadcaster = new Broadcaster();
            database = new DBManager("$objectdb/db/buckets.odb");
            
            ui = new BucketsUI(broadcaster);
            
            watcher = database.loadWatcher();
            watcher.setRules( database.loadRules() );
            watcher.init(broadcaster);
            
            ui.initCustom();
            watcher.start();
	}
	
	public void run () {
            broadcaster.subscribe( watcher, ui, this );
            EventQueue.invokeLater(() -> { this.ui.setVisible(true); });
		
            this.broadcaster.broadcast(new BucketsEvent(EventType.INIT_ALL));
	}
        
        private void onShutdown() {
            RuleSet rules = watcher.getRules();
            database.clear();
            database.save(rules);
            database.save(watcher);
            database.exit();
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
