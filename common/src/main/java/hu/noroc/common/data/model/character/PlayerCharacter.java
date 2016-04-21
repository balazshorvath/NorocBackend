package hu.noroc.common.data.model.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.noroc.common.data.model.InventoryItem;
import hu.noroc.common.data.model.spell.CharacterSpell;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oryk on 1/11/2016.
 */
public class PlayerCharacter {
    private String id;
    private String name;
    private String userId;
    private String classId;
    private long xp = 0;
    private List<InventoryItem> inventory = new ArrayList<>();
    private Map<String, CharacterSpell> spells = new HashMap<>();

    private double x, y;

    public PlayerCharacter() {
    }

    public PlayerCharacter(String name, String userId, String classId) {
        this.name = name;
        this.userId = userId;
        this.classId = classId;
    }

    @org.mongojack.ObjectId
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @org.mongojack.ObjectId
    @JsonProperty("_id")
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public long getXp() {
        return xp;
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

    public Map<String, CharacterSpell> getSpells() {
        return spells;
    }

    public void setSpells(Map<String, CharacterSpell> spells) {
        this.spells = spells;
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
