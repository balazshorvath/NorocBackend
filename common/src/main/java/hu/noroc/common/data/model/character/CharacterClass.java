package hu.noroc.common.data.model.character;

import java.util.List;

/**
 * Created by Oryk on 1/11/2016.
 */
public class CharacterClass {
    private String id;
    private String name;
    private CharacterStat stat;
    private List<String> spells;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CharacterStat getStat() {
        return stat;
    }

    public void setStat(CharacterStat stat) {
        this.stat = stat;
    }

    public List<String> getSpells() {
        return spells;
    }

    public void setSpells(List<String> spells) {
        this.spells = spells;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
