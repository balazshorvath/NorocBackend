package hu.noroc.common.communication.message;

import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import org.codehaus.jackson.annotate.JsonSubTypes;

/**
 * Created by Oryk on 4/28/2016.
 */
@JsonSubTypes({@JsonSubTypes.Type(value = AttackMessage.class, name = "AttackMessage")})
public class DirectionalMessage extends Message {
    protected double x, y;
    protected double xFrom, yFrom;
    protected DirectionalEvent.DirectionalType directionalType;

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

    public double getxFrom() {
        return xFrom;
    }

    public void setxFrom(double xFrom) {
        this.xFrom = xFrom;
    }

    public double getyFrom() {
        return yFrom;
    }

    public void setyFrom(double yFrom) {
        this.yFrom = yFrom;
    }

    public DirectionalEvent.DirectionalType getDirectionalType() {
        return directionalType;
    }

    public void setDirectionalType(DirectionalEvent.DirectionalType directionalType) {
        this.directionalType = directionalType;
    }
}
