package hu.noroc.common.data.model;

/**
 * Created by Oryk on 4/1/2016.
 */
public class InventoryItem {
    private String itemId;
    private int slot;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
