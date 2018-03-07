package com.ninjaphase.pokered.editor.data.entity.event.node;

import com.ninjaphase.pokered.editor.data.entity.event.EntityEvent;

/**
 * <p>
 *     A {@code EventNode} is a node within the event editor, it is done this way to allow for sub-events for the
 *     if statements (When/If they get added).
 * </p>
 */
public class EventNode {

    private EntityEvent ev;

    public EventNode(EntityEvent ev) {
        this.ev = ev;
    }

    @Override
    public String toString() {
        if(ev == null) {
            return "@>";
        } else {
            return "@>" + ev.toNodeString();
        }
    }

}
