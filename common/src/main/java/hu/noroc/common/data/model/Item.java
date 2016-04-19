package hu.noroc.common.data.model;

import hu.noroc.common.data.model.character.CharacterStat;

/**
 * Created by Oryk on 4/1/2016.
 */
public class Item {
    private String id;
    private String name;
    private CharacterStat stat;

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

    public CharacterStat getStat() {
        return stat;
    }

    public void setStat(CharacterStat stat) {
        this.stat = stat;
    }
}
