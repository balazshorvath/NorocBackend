package hu.noroc.common.communication.message.models;

import hu.noroc.common.data.model.InventoryItem;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oryk on 2016. 05. 28..
 */
public class PlayerCharacterResponse {
    private String id;
    private String name;
    private String userId;
    private String classId;
    private CharacterStat stat;
    private int maxHealth;
    private int maxMana;
    private long xp = 0;
    private List<InventoryItem> inventory = new ArrayList<>();
    private List<CharacterSpell> spells = new ArrayList<>();

    private double x, y;

    public PlayerCharacterResponse(PlayerCharacter character, int hp, int mana, CharacterStat stat) {
        this.id = character.getId();
        this.name = character.getName();
        this.userId = character.getUserId();
        this.classId = character.getClassId();
        this.xp = character.getXp();
        this.inventory = character.getInventory();
        character.getSpells().forEach((s, characterSpell) -> this.spells.add(characterSpell));
        this.x = character.getX();
        this.y = character.getY();
        this.maxHealth = hp;
        this.maxMana = mana;
        this.stat = stat;
    }

    public CharacterStat getStat() {
        return stat;
    }

    public void setStat(CharacterStat stat) {
        this.stat = stat;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
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

    public List<CharacterSpell> getSpells() {
        return spells;
    }

    public void setSpells(List<CharacterSpell> spells) {
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
