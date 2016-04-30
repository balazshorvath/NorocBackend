package hu.noroc.common.communication.request;

/**
 * Created by Oryk on 4/30/2016.
 */
public class PauseRequest extends Request {
    public PauseRequest() {
        super.type = PauseRequest.class.getSimpleName();
    }
}
