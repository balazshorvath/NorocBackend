package hu.noroc.common.data.model.spell;

import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.components.behaviour.spell.DamageLogic;
import hu.noroc.gameworld.components.behaviour.spell.OverTimeLogic;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;

/**
 * Stats property which stats need to be changed in case someone is effected by the spell.
 * <p>
 * Spells can have mixed type. In that case the opposite of the main type (BUFF-DEBUFF) also happens.
 * <p>
 * Created by Oryk on 4/1/2016.
 */
public class SpellEffect {
    protected CharacterStat stat;
    protected SpellType type;
    protected DamageType damageType;

    protected boolean mixed;

    /* For stuns, HOTs and DOTs */
    /* Defined in "ticks". tick time: Ticker.TICK_UNIT */
    protected int duration;
    protected int period;

    public SpellEffect(SpellEffect spellEffect) {
        this.stat = spellEffect.stat.copy();
        this.type = spellEffect.type;
        this.damageType = spellEffect.damageType;
        this.mixed = spellEffect.mixed;
        this.duration = spellEffect.duration;
        this.period = spellEffect.period;

    }

    public SpellLogic createLogic(String caster, String spellId, String spellName) {
        switch (type) {
            case BUFF:
            case DEBUFF:
            case STUN:
                return new BuffLogic(this, spellName, spellId, caster);
            case DOT:
            case HOT:
                return new OverTimeLogic(this, spellName, spellId, caster);
            case DAMAGE:
            case HEAL:
                return new DamageLogic(this, spellName, spellId, caster);
        }
        // impossibru
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpellEffect that = (SpellEffect) o;

        return mixed == that.mixed && duration == that.duration
                && period == that.period && type == that.type && damageType == that.damageType;

    }

    @Override
    public int hashCode() {
        int result = 31 * type.hashCode();
        result = 31 * result + (damageType != null ? damageType.hashCode() : 0);
        result = 31 * result + (mixed ? 1 : 0);
        result = 31 * result + duration;
        result = 31 * result + period;
        return result;
    }

    public SpellEffect() {
    }

    public SpellEffect(CharacterStat stat, SpellType type,
            DamageType damageType, boolean mixed, int duration, int period) {
        this.stat = stat;
        this.type = type;
        this.damageType = damageType;
        this.mixed = mixed;
        this.duration = duration;
        this.period = period;
    }

    public enum SpellType {
        BUFF,
        DEBUFF,

        DOT,
        HOT,

        DAMAGE,
        HEAL,

        STUN
    }

    public enum DamageType {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }
}
