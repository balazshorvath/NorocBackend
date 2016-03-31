package hu.noroc.gameworld.messaging.player;

import hu.noroc.gameworld.messaging.EntityType;
import hu.noroc.gameworld.messaging.EventMessage;

/**
 * Created by Oryk on 3/19/2016.
 */
public class PlayerEvent extends EventMessage {
    protected String session;
    /* current position of the player*/

    public PlayerEvent() {
        super.entity = EntityType.PLAYER;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

}
