package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 3/28/2016.
 */
public class ListWorldsRequest extends Request {
    public ListWorldsRequest() {
        this.type = ListWorldsRequest.class.getSimpleName();
    }
}
