package hu.noroc.gameworld.messaging.sync;

import hu.noroc.gameworld.messaging.Event;

/**
 * This message type is transferred between the entry-point's Client and it's Player object.
 *
 * Created by Oryk on 4/5/2016.
 */
public class SyncMessage {
    private String session;
    private Event event;

    public SyncMessage(String session, Event event) {
        this.session = session;
        this.event = event;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
