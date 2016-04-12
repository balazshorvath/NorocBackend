package hu.noroc.gameworld.messaging.directional;

import hu.noroc.gameworld.messaging.EventMessage;

/**
 * x, y - direction, connect with subtype's being x, y
 *
 * Created by Oryk on 4/3/2016.
 */
public class DirectionalEvent extends EventMessage {
    protected double x, y;
    protected DirectionalType directionalType;


    public enum DirectionalType{
        CURRENTLY_AT,
        MOVING_TO,
        ATTACK
    }

    public DirectionalType getDirectionalType() {
        return directionalType;
    }

    public void setDirectionalType(DirectionalType directionalType) {
        this.directionalType = directionalType;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
