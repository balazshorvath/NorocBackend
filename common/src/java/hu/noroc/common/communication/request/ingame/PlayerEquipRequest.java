package hu.noroc.common.communication.request.ingame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 4/3/2016.
 */
public class PlayerEquipRequest extends Request {
    private String itemId;

    public PlayerEquipRequest() {
        this.type = PlayerEquipRequest.class.getSimpleName();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
