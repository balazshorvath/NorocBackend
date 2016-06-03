package hu.noroc.gameworld.messaging.directional;

import hu.noroc.common.communication.message.DirectionalMessage;
import hu.noroc.common.communication.message.Message;
import hu.noroc.gameworld.messaging.Event;

/**
 * x, y - direction, connect with subtype's being x, y
 *
 * Created by Oryk on 4/3/2016.
 */
public class DirectionalEvent extends Event {
    protected double x, y;
    protected DirectionalType directionalType;

    @Override
    public Message createMessage() {
        DirectionalMessage message = new DirectionalMessage();

        message.setDirectionalType(directionalType);
        message.setX(x);
        message.setY(y);
        if(directionalType.equals(DirectionalType.MOVING_TO)){
            message.setxFrom(being.getX());
            message.setyFrom(being.getY());
        }
        message.setEntityId(being.getId());
        message.setEntityType(entity);

        return message;
    }


    public enum DirectionalType{
        MOVING_TO,

        ATTACK,
        CAST
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
