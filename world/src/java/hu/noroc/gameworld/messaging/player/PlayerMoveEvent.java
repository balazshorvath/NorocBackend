package hu.noroc.gameworld.messaging.player;

import hu.noroc.gameworld.messaging.EntityActivityType;

/**
 * Created by Oryk on 3/19/2016.
 */
public class PlayerMoveEvent extends PlayerEvent {
    protected float x1, y1;

    public PlayerMoveEvent() {
        super();
        super.activity = EntityActivityType.MOV;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }
}
