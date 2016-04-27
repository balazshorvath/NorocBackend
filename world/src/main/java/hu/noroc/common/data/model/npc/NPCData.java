package hu.noroc.common.data.model.npc;

import hu.noroc.common.data.model.character.CharacterStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/17/2016.
 */
public class NPCData {
    private String name;
    private CharacterStat stats;
    private List<String> spells = new ArrayList<>();
    private double x, y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterStat getStats() {
        return stats;
    }

    public void setStats(CharacterStat stats) {
        this.stats = stats;
    }

    public List<String> getSpells() {
        return spells;
    }

    public void setSpells(List<String> spells) {
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
