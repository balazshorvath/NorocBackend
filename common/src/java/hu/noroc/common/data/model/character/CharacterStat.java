package hu.noroc.common.data.model.character;


/**
 * Created by Oryk on 3/20/2016.
 */
public class CharacterStat {
    public int health, mana;
    public int armor, magicResist;
    public int strength, stamina;
    public int intellect, spirit;
    public double speed;

    public CharacterStat() {
    }

    public CharacterStat(CharacterStat characterStat){
        this.health = characterStat.health;
        this.mana = characterStat.mana;
        this.armor = characterStat.armor;
        this.magicResist = characterStat.magicResist;
        this.strength = characterStat.strength;
        this.stamina = characterStat.stamina;
        this.intellect = characterStat.intellect;
        this.spirit = characterStat.spirit;
        this.speed = characterStat.speed;

    }
    public CharacterStat copy(){
        return new CharacterStat(this);
    }
}
