package hu.noroc.common.communication.message;

/**
 * This type of message is sent, whenever an entity (by now the abstraction is called Being)
 * attacks another entity.
 *
 *
 * Created by Oryk on 3/28/2016.
 */
public class AttackMessage extends DirectionalMessage {
    private String spellId;

    public AttackMessage() {
        this.type = AttackMessage.class.getSimpleName();
    }

    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(String spellId) {
        this.spellId = spellId;
    }

}
