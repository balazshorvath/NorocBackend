package hu.noroc.gameworld.messaging.targeted;

import hu.noroc.gameworld.messaging.EventMessage;

/**
 * Created by Oryk on 4/3/2016.
 */
public class TargetedEvent extends EventMessage {
    protected String targetId;
    protected TargetedType targetedType;

    public enum TargetedType{
        INTERACT,
        EQUIP
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public TargetedType getTargetedType() {
        return targetedType;
    }

    public void setTargetedType(TargetedType targetedType) {
        this.targetedType = targetedType;
    }
}
