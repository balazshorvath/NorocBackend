package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.ClearMessage;
import hu.noroc.common.communication.message.Message;

/**
 * Created by Oryk on 2016. 05. 29..
 */
public class AreaChangedEvent extends Event {
    public AreaChangedEvent() {
        super();
    }

    @Override
    public Message createMessage() {
        ClearMessage message = new ClearMessage();
        message.setTimestamp(timestamp);
        return message;
    }
}
