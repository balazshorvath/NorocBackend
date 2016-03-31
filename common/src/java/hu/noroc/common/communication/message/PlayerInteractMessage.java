package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 3/28/2016.
 */
public class PlayerInteractMessage extends Message {
    private String targetId;

    public PlayerInteractMessage() {
        this.type = PlayerInteractMessage.class.getSimpleName();
        this.entityType = "Player";
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
