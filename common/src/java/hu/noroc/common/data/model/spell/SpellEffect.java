package hu.noroc.common.data.model.spell;

import hu.noroc.common.data.model.character.CharacterStat;

/**
 * Stats property which stats need to be changed in case someone is effected by the spell.
 *
 * Spells can have mixed type. In that case the opposite of the main type (BUFF-DEBUFF) also happens.
 *
 * Created by Oryk on 4/1/2016.
 */
public class SpellEffect {
    private CharacterStat stats;
    private SpellType mainType;
    private CharacterStat subStats;
    private SpellType subtype;
    private boolean mixed;

    /*For HOTs and DOTs*/
    private long duration;
    private int period;

    public enum SpellType{
        BUFF,
        DEBUFF,

        DOT,
        HOT,

        ATTACK,
        HEAL
    }

    public CharacterStat getStats() {
        return stats;
    }

    public void setStats(CharacterStat stats) {
        this.stats = stats;
    }

    public SpellType getMainType() {
        return mainType;
    }

    public void setMainType(SpellType mainType) {
        this.mainType = mainType;
    }

    public CharacterStat getSubStats() {
        return subStats;
    }

    public void setSubStats(CharacterStat subStats) {
        this.subStats = subStats;
    }

    public SpellType getSubtype() {
        return subtype;
    }

    public void setSubtype(SpellType subtype) {
        this.subtype = subtype;
    }

    public boolean isMixed() {
        return mixed;
    }

    public void setMixed(boolean mixed) {
        this.mixed = mixed;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
