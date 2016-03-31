package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * When the client logs in with one of it's characters, this request is sent by the client
 * to get all the information around the current character.
 *
 * Created by Oryk on 3/28/2016.
 */
public class InitRequest extends Request {
    public InitRequest() {
        this.type = InitRequest.class.getSimpleName();
    }
}
