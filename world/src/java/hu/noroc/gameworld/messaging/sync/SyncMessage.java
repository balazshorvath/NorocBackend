package hu.noroc.gameworld.messaging.sync;

import hu.noroc.gameworld.messaging.EventMessage;

/**
 * This message type is transferred between the entry-point's Client and it's Player object.
 *
 * Created by Oryk on 4/5/2016.
 */
public class SyncMessage {
    private String session;
    private EventMessage message;

    public SyncMessage(String session, EventMessage message) {
        this.session = session;
        this.message = message;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public EventMessage getMessage() {
        return message;
    }

    public void setMessage(EventMessage message) {
        this.message = message;
    }
}
