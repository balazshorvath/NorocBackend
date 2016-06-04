package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.EntityType;
import hu.noroc.common.communication.message.Message;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * being - who did this.
 * entity - entity type.
 * activity - activity type.
 *
 * Created by Oryk on 3/19/2016.
 */
public abstract class Event {
    protected EntityType entity;
    protected EntityActivityType activity;
    protected Being being;
    protected Long timestamp;

    public Event() {
        timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public abstract Message createMessage();

    public EntityType getEntity() {
        return entity;
    }

    public EntityActivityType getActivity() {
        return activity;
    }

    public void setActivity(EntityActivityType activity) {
        this.activity = activity;
    }

    public void setEntity(EntityType entity) {
        this.entity = entity;
    }

    public Being getBeing() {
        return being;
    }

    public void setBeing(Being being) {
        this.being = being;
    }
}
