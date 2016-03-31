package hu.noroc.gameworld.messaging;

/**
 * Created by Oryk on 3/19/2016.
 */
public class EventMessage {
    protected EntityType entity;
    protected EntityActivityType activity;
    protected float x, y;

    public EntityType getEntity() {
        return entity;
    }

    public EntityActivityType getActivity() {
        return activity;
    }

    public void setActivity(EntityActivityType activity) {
        this.activity = activity;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
