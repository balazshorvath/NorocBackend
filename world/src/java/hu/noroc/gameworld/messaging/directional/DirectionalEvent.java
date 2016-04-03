package hu.noroc.gameworld.messaging.directional;

import hu.noroc.gameworld.messaging.EventMessage;

/**
 * x, y - direction, connect with subtype's being x, y
 *
 * Created by Oryk on 4/3/2016.
 */
public class DirectionalEvent  extends EventMessage {
    protected double x, y;

    public enum DirectionalType{
        CURRENTLY_AT,
        MOVING_TO,
        ATTACK
    }
}
