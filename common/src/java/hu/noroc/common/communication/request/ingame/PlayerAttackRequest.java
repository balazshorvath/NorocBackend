package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * When a player casts a spell, this request is sent by the client.
 *
 * Created by Oryk on 3/28/2016.
 */
public class PlayerAttackRequest extends Request {
    private String targetId;
    private String spellId;

    public PlayerAttackRequest() {
        this.type = PlayerAttackRequest.class.getSimpleName();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(String spellId) {
        this.spellId = spellId;
    }
}
