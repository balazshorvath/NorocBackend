package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 3/28/2016.
 */
public class PlayerInteractRequest extends Request {
    private String targetId;

    public PlayerInteractRequest() {
        this.type = PlayerInteractRequest.class.getSimpleName();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
