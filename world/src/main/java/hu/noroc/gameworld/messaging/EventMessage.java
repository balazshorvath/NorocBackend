package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.EntityType;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * being - who did this.
 * entity - entity type.
 * activity - activity type.
 *
 * Created by Oryk on 3/19/2016.
 */
public abstract class EventMessage {
    protected EntityType entity;
    protected EntityActivityType activity;
    protected Being being;

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
