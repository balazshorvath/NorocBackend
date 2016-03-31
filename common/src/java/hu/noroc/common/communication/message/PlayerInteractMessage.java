package hu.noroc.common.communication.message;

/**
 * Whenever a player interacts with a friendly NPC, this message should be sent to
 * all the players inside the current Area.
 *
 * The message does not contain much useful information.
 * May be useful later though..
 *
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
