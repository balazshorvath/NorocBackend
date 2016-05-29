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
    private Vector[] path;

    public PlayerMoveRequest() {
        this.type = PlayerMoveRequest.class.getSimpleName();
    }

    public Vector[] getPath() {
        return path;
    }

    public void setPath(Vector[] path) {
        this.path = path;
    }

    public static class Vector{
        private double x, y;

        public Vector() {
        }

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
