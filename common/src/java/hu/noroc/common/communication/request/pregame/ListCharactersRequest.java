package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Request for listing the User's characters.
 *
 * Created by Oryk on 3/28/2016.
 */
public class ListCharactersRequest extends Request {
    public ListCharactersRequest() {
        this.type = ListCharactersRequest.class.getSimpleName();
    }
}
