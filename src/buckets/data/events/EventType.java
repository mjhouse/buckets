/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buckets.data.events;

/**
 *
 * @author mhouse
 */
public enum EventType {

    /**
     * Requested add directory.
     */
    ADD_DIRECTORY,
    /**
     * Requested delete directory.
     */
    DEL_DIRECTORY,
    /**
     * Directory deleted.
     */
    DIRECTORY_DEL,
    /**
     * Directory added.
     */
    DIRECTORY_ADD,
    /**
     * Requested add rule.
     */
    ADD_RULE,
    /**
     * Requested delete rule.
     */
    DEL_RULE,
    /**
     * Rule deleted.
     */
    RULE_DEL,
    /**
     * Rule added.
     */
    RULE_ADD,
    /**
     * Initialize everything (on start).
     */
    INIT_ALL,
    /**
     * Shutdown everything (on exit).
     */
    EXIT,
}
