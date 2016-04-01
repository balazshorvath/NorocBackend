package hu.noroc.common.data.model.character;

import hu.noroc.common.data.model.InventoryItem;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 1/11/2016.
 */
public class Character {
    private String id = new ObjectId().toString();
    private String name;
    private String userId;
    private Long classId;
    private long xp = 0;
    private List<InventoryItem> inventory = new ArrayList<>();

    public Character() {
    }

    public Character(String name, String userId, Long classId) {
        this.name = name;
        this.userId = userId;
        this.classId = classId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<InventoryItem> inventory) {
        this.inventory = inventory;
    }
}
