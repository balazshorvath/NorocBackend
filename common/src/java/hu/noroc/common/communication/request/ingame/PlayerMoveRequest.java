package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 3/28/2016.
 */
public class PlayerMoveRequest extends Request {
    private double[][] path;

    public PlayerMoveRequest() {
        this.type = PlayerMoveRequest.class.getSimpleName();
    }

    public double[][] getPath() {
        return path;
    }

    public void setPath(double[][] path) {
        this.path = path;
    }
}
