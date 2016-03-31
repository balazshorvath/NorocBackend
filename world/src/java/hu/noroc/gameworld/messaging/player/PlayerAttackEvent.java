package hu.noroc.gameworld.messaging.player;

import hu.noroc.gameworld.messaging.EntityActivityType;

/**
 * Created by Oryk on 3/19/2016.
 */
public class PlayerAttackEvent extends PlayerEvent {
    protected String targetId;
    protected String spellId;

    public PlayerAttackEvent() {
        super();
        super.activity = EntityActivityType.ATTACK;
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
