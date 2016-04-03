package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 4/3/2016.
 */
public class PlayerEquipMessage extends Message {
    private String itemId;
    public PlayerEquipMessage() {
        this.type = PlayerEquipMessage.class.getSimpleName();
        this.entityType = EntityType.PLAYER;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
