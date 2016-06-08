package hu.noroc.common.communication.message;

import hu.noroc.gameworld.messaging.targeted.TargetedEvent;

/**
 * Created by Oryk on 4/28/2016.
 */
public class TargetedMessage extends Message {
    protected String targetId;
    protected TargetedEvent.TargetedType targetedType;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public TargetedEvent.TargetedType getTargetedType() {
        return targetedType;
    }

    public void setTargetedType(TargetedEvent.TargetedType targetedType) {
        this.targetedType = targetedType;
    }
}
