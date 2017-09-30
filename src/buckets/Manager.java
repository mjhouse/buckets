/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.Subscriber;
import buckets.ui.BucketsUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

/**
 *
 * @author mhouse
 */
public class Manager implements Subscriber<ActionEvent> {
	private BucketsUI ui;
	private Watcher watcher;
	
	public Manager () {
		watcher = new Watcher();
		ui = new BucketsUI();
		
		ui.broadcast.subscribe("watchAddActionPerformed",watcher);
	}
	
	public void run () {
		ui.broadcast.subscribe("watchAddActionPerformed",this);
		EventQueue.invokeLater(() -> { this.ui.setVisible(true); });
	}
	
	@Override
	public void notify ( String name, ActionEvent e ) {
		System.out.println(name);
	}
}
