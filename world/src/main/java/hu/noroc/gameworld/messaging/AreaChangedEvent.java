package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.ClearMessage;
import hu.noroc.common.communication.message.Message;

/**
 * Created by Oryk on 2016. 05. 29..
 */
public class AreaChangedEvent extends Event {

    @Override
    public Message createMessage() {
        return new ClearMessage();
    }
}
