package hu.noroc.common.communication.message;

import hu.noroc.gameworld.messaging.InitEvent;

/**
 * Created by Oryk on 2016. 05. 28..
 */
public class InitMessage extends Message {
    private InitEvent.InGamePlayer self;

    public InitEvent.InGamePlayer getSelf() {
        return self;
    }

    public void setSelf(InitEvent.InGamePlayer self) {
        this.self = self;
    }
}
