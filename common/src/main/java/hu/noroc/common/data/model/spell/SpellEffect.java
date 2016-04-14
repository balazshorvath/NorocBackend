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
    private CharacterStat stat;
    private SpellType type;
    private DamageType damageType;

    private boolean mixed;

    /*For stuns, HOTs and DOTs*/
    private long duration;
    private int period;

    public SpellEffect(SpellEffect spellEffect) {
        this.stat = spellEffect.stat.copy();
        this.type = spellEffect.type;
        this.damageType = spellEffect.damageType;
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

        DAMAGE,
        HEAL,

        STUN
    }
    public enum DamageType{
        MAGIC,
        PHYSICAL
    }

    public CharacterStat getStat() {
        return stat.copy();
    }

    public void setStat(CharacterStat stat) {
        this.stat = stat;
    }

    public SpellType getType() {
        return type;
    }

    public void setType(SpellType type) {
        this.type = type;
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

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }
}
