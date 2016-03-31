package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 3/28/2016.
 */
public class EntityAttackMessage extends Message {
    private String targetId;
    private String spellId;

    public EntityAttackMessage() {
        this.type = EntityAttackMessage.class.getSimpleName();
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
