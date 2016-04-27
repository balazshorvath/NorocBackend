package hu.noroc.common.communication.message;

/**
 * This type of message is sent, whenever an entity (by now the abstraction is called Being)
 * attacks another entity.
 *
 *
 * Created by Oryk on 3/28/2016.
 */
public class EntityAttackMessage extends Message {
    private double x, y;
    private String spellId;

    public EntityAttackMessage() {
        this.type = EntityAttackMessage.class.getSimpleName();
    }

    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(String spellId) {
        this.spellId = spellId;
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
