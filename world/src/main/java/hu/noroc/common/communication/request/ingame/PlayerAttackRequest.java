package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * When a player casts a spell, this request is sent by the client.
 *
 * Created by Oryk on 3/28/2016.
 */
public class PlayerAttackRequest extends Request {
    private double x, y;
    private String spellId;

    public PlayerAttackRequest() {
        this.type = PlayerAttackRequest.class.getSimpleName();
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

    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(String spellId) {
        this.spellId = spellId;
    }
}
