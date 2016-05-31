package hu.noroc.common.communication.message;

import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import org.codehaus.jackson.annotate.JsonSubTypes;

/**
 * Created by Oryk on 4/28/2016.
 */
@JsonSubTypes({@JsonSubTypes.Type(value = AttackMessage.class, name = "AttackMessage")})
public class DirectionalMessage extends Message {
    protected double x, y;
    protected DirectionalEvent.DirectionalType directionalType;

    public DirectionalMessage() {
        this.type = DirectionalMessage.class.getSimpleName();
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

    public DirectionalEvent.DirectionalType getDirectionalType() {
        return directionalType;
    }

    public void setDirectionalType(DirectionalEvent.DirectionalType directionalType) {
        this.directionalType = directionalType;
    }
}
