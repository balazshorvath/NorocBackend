package hu.noroc.common.communication.message;

import hu.noroc.gameworld.messaging.InitResponse;

/**
 * Created by Oryk on 2016. 05. 28..
 */
public class InitMessage extends Message {
    private InitResponse.InGamePlayer self;

    public InitResponse.InGamePlayer getSelf() {
        return self;
    }

    public void setSelf(InitResponse.InGamePlayer self) {
        this.self = self;
    }
}
