package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 3/28/2016.
 */
public class InitRequest extends Request {
    public InitRequest() {
        this.type = InitRequest.class.getSimpleName();
    }
}
