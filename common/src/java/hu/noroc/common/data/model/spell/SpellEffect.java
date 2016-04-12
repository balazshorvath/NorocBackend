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
    private CharacterStat mainStats;
    private SpellType mainType;
    private DamageType mainDamageType;

    private CharacterStat subStats;
    private SpellType subtype;
    private DamageType subDamageType;
    private boolean mixed;

    /*For HOTs and DOTs*/
    private long duration;
    private int period;

    public SpellEffect(SpellEffect spellEffect) {
        this.mainStats = spellEffect.mainStats.copy();
        this.mainType = spellEffect.mainType;
        this.mainDamageType = spellEffect.mainDamageType;
        this.subStats = spellEffect.subStats.copy();
        this.subtype = spellEffect.subtype;
        this.subDamageType = spellEffect.subDamageType;
        this.mixed = spellEffect.mixed;
        this.duration = spellEffect.duration;
        this.period = spellEffect.period;

    }

    public SpellEffect() {
    }

    public enum SpellType{
        BUFF,
        DEBUFF,

        DOT,
        HOT,

        ATTACK,
        HEAL
    }
    public enum DamageType{
        MAGIC,
        PHYSICAL
    }

    public CharacterStat getMainStats() {
        return mainStats.copy();
    }

    public void setMainStats(CharacterStat mainStats) {
        this.mainStats = mainStats;
    }

    public SpellType getMainType() {
        return mainType;
    }

    public void setMainType(SpellType mainType) {
        this.mainType = mainType;
    }

    public CharacterStat getSubStats() {
        return subStats.copy();
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

    public DamageType getMainDamageType() {
        return mainDamageType;
    }

    public void setMainDamageType(DamageType mainDamageType) {
        this.mainDamageType = mainDamageType;
    }

    public DamageType getSubDamageType() {
        return subDamageType;
    }

    public void setSubDamageType(DamageType subDamageType) {
        this.subDamageType = subDamageType;
    }
}
