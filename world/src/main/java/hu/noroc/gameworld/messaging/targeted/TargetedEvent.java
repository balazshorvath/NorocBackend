package hu.noroc.gameworld.messaging.targeted;

import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.message.TargetedMessage;
import hu.noroc.gameworld.messaging.Event;

/**
 * Created by Oryk on 4/3/2016.
 */
public class TargetedEvent extends Event {
    protected String targetId;
    protected TargetedType targetedType;

    public TargetedEvent() {
        super();
    }

    @Override
    public Message createMessage() {
        TargetedMessage message = new TargetedMessage();

        message.setTimestamp(timestamp);

        message.setEntityId(being.getId());
        message.setEntityType(entity);
        message.setTargetId(targetId);
        message.setTargetedType(targetedType);

        return message;
    }

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
