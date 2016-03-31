package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * When a player moves, this request is sent to the server.
 *
 * Path it calculated by the client and is separated into smaller "checkpoints",
 * which checkpoints can be used to sync the movement on the clients (also preventing speedhacking)
 * and giving information to NPCs for example.
 *
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
