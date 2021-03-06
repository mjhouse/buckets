/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets;

import buckets.data.events.*;

import buckets.data.Broadcaster;

import buckets.actions.Move;
import buckets.rules.Rule;
import buckets.rules.RuleSet;
import buckets.data.Subscriber;

// data structures
import java.util.ArrayList;

// file/path objects
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

// watch events
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchService;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;

// async
import java.util.concurrent.CompletableFuture;

// logging imports
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * watches a given collection of directories and notifies the rule manager when
 * files are created.
 *
 * @author mhouse
 */
@Entity
public class Watcher implements Subscriber, Serializable {

    transient private static final Logger log = Logger.getLogger("buckets.watcher");

    transient private Broadcaster broadcaster;
    private ArrayList<String> directories;
    transient private ArrayList<String> backlog;
    transient private RuleSet rules;
    transient private WatchService watcher;
    transient private Boolean running;

    @Id
    @GeneratedValue
    private long id;

    /**
     * No-arg constructor to init the various collections within the Watcher to
     * default values.
     */
    public Watcher() {
        broadcaster = null;
        directories = new ArrayList();
        backlog = new ArrayList();
        rules = new RuleSet();
        running = false;
    }

    /**
     * Constructor with arguments.
     *
     * @param b the Broadcaster to subscribe/broadcast with.
     * @param dirs directories to watch
     */
    public Watcher(Broadcaster b, String... dirs) {
        directories = new ArrayList();
        backlog = new ArrayList();
        rules = new RuleSet();
        running = false;

        init(b, dirs);
    }

    /**
     * manaually init with a Broadcaster and starting directories.
     *
     * @param b the Broadcaster to subscribe/broadcast with.
     * @param dirs variable collection of String directories.
     */
    public void init(Broadcaster b, String... dirs) {
        broadcaster = b;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            for (String s : dirs) {
                Path absp = Paths.get(s).toAbsolutePath();
                this.addWatched(absp.toString());
                log.info("watching dirs");
            }
        } catch (IOException e) {
        }
    }

    /**
     * Another manual init method.
     *
     * @param b the Broadcaster to subscribe/broadcast with.
     */
    public void init(Broadcaster b) {
        broadcaster = b;
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
        }
        this.updateWatched();
    }

    /**
     * Override the Subscriber interface to receive BucketsEvents.
     *
     * @param e the BucketsEvent to handle.
     */
    @Override
    public void notify(BucketsEvent e) {
        EventData data, regex, path, idx;
        switch (e.type()) {
            case ADD_DIRECTORY:
                data = e.get("path");
                if (data != null && this.addWatched(data.asString())) {
                    this.broadcaster.broadcast(new BucketsEvent(EventType.DIRECTORY_ADD));
                }
                break;
            case DEL_DIRECTORY:
                data = e.get("path");
                System.out.println(data.asString());
                if (data != null && this.removeWatched(data.asString())) {
                    this.broadcaster.broadcast(new BucketsEvent(EventType.DIRECTORY_DEL));
                }
                break;
            case ADD_RULE:
                regex = e.get("regex");
                path = e.get("path");
                if (regex != null && path != null && this.addRule(regex.asString(), path.asString())) {
                    this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_ADD));
                }
                break;
            case DEL_RULE:
                regex = e.get("regex");
                path = e.get("path");
                idx = e.get("index");
                if (regex != null && path != null && this.removeRule(regex.asString(), path.asString())) {
                    this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_DEL));
                } else if (idx != null) {
                    Rule r = this.removeRule(idx.asInt());
                    if (r != null) {
                        this.broadcaster.broadcast(new BucketsEvent(EventType.RULE_DEL)
                            .add(new EventData("id", r.getId())));
                    }
                }
                break;
            case EXIT:
                this.stop();
                break;
        }
    }

    /**
     * begin watching directories
     */
    public void start() {
        if (!running) {
            running = true;
            log.info("watching directories");
            CompletableFuture.runAsync(this::run);
        }
    }

    /**
     * stop watching directories
     */
    public void stop() {
        running = false;
        log.info("stopping");
    }

    /**
     * Begins watching the target directories, and passes new file events to the
     * RuleSet.
     */
    private void run() {
        WatchKey key;
        while (running) {

            if (backlog.size() > 0) {
                ArrayList<String> rmlist = new ArrayList();
                for (String s : backlog) {
                    if (!isChanging(s)) {
                        Path p = Paths.get(s);
                        rmlist.add(s);
                        rules.apply(p);
                    }
                }
                backlog.removeAll(rmlist);
            }

            // poll for events from watcher
            if ((key = watcher.poll()) == null) {
                continue;
            }

            // if an event is found, process it
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info("event received");

                // get the environment from the event
                WatchEvent<Path> ev = (WatchEvent<Path>) event;

                // get the directory the event occurs in
                Path dir = (Path) key.watchable();
                if (directories.contains(dir.toString())) {
                    // build an absolute path with the directory and 
                    // file name.
                    Path abspath = dir.resolve(ev.context());
                    String abstr = abspath.toString();
                    if (!isChanging(abstr)) {
                        // do something with the absolute path.
                        rules.apply(abspath);
                    } else if (!backlog.contains(abstr)) {
                        backlog.add(abstr);
                    }
                } else {
                    key.cancel();
                }
            }

            if (!key.reset()) {
                log.info("watchkey is bad");
                this.stop();
            }
        }
    }

    private Boolean isChanging(String p) {
        try {
            File file = new File(p);
            long first = file.lastModified();
            Thread.sleep(4000);
            long last = file.lastModified();
            if (last == first) {
                return false;
            }
            return true;
        } catch (InterruptedException err) {
            return true;
        }
    }

    /**
     * Get watched directories.
     *
     * @return the directories being watched
     */
    public ArrayList<String> getWatched() {
        return directories;
    }

    /**
     * Set watched directories.
     *
     * @param p paths to begin watching
     */
    public void setWatched(ArrayList<String> p) {
        directories = p;
    }

    /**
     * Add a new directory to watch.
     *
     * @param s path to watch
     * @return whether path was successfully added.
     */
    public Boolean addWatched(String s) {
        if (!directories.contains(s)) {
            try {
                Path p = Paths.get(s).toAbsolutePath();
                p.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
                directories.add(p.toString());
                return true;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return false;
    }

    /**
     * Register to get modify and create events for the current set of
     * directories.
     */
    public void updateWatched() {
        for (String s : directories) {
            try {
                Path p = Paths.get(s);
                p.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Remove a given path.
     *
     * @param s path to remove.
     * @return whether path was removed.
     */
    public Boolean removeWatched(String s) {
        if (directories.contains(s)) {
            directories.remove(s);
            return true;
        }
        return false;
    }

    /**
     * Get the WatchService.
     *
     * @return the WatchService being used
     */
    public WatchService getWatcher() {
        return watcher;
    }

    /**
     * Set a new WatchService.
     *
     * @param w the WatchService to use.
     */
    public void setWatcher(WatchService w) {
        watcher = w;
    }

    /**
     * get the current RuleSet
     *
     * @return the current set of rules
     */
    public RuleSet getRules() {
        return rules;
    }

    /**
     * use a new RuleSet
     *
     * @param r the RuleSet to use
     */
    public void setRules(RuleSet r) {
        rules = r;
    }

    /**
     * Create and add a new Rule.
     *
     * @param r rule pattern.
     * @param p the rule path.
     * @return whether the rule was added.
     */
    public Boolean addRule(String r, String p) {
        Rule rule = new Rule(r, new Move(p));
        return rules.add(rule);
    }

    /**
     * Remove a Rule.
     *
     * @param r the rule pattern.
     * @param p the rule path.
     * @return whether the rule was removed.
     */
    public Boolean removeRule(String r, String p) {
        Rule rule = new Rule(r, new Move(p));
        return rules.remove(rule);
    }

    /**
     * Remove a Rule by index.
     *
     * @param idx index of rule to remove.
     * @return the removed rule.
     */
    public Rule removeRule(Integer idx) {
        return rules.remove(idx);
    }

}
