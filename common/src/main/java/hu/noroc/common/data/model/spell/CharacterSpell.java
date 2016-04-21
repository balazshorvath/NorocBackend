package hu.noroc.common.data.model.spell;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/14/2016.
 */
public class CharacterSpell extends Spell {
    /* in-game properties */
    private long nextCast;
    /* upgrading */
    private int upgradesSpent;
    private List<SpellEffect> effects = new ArrayList<>();

    public CharacterSpell() {
    }

    public CharacterSpell(Spell spell) {
        super(spell);
    }

    @JsonIgnore
    public void setCooldown(long now){
        this.nextCast = now  + this.cooldown;
    }

    @JsonIgnore
    public long getNextCast() {
        return nextCast;
    }

    public void setNextCast(long nextCast) {
        this.nextCast = nextCast;
    }

    public int getUpgradesSpent() {
        return upgradesSpent;
    }

    public void setUpgradesSpent(int upgradesSpent) {
        this.upgradesSpent = upgradesSpent;
    }

    public List<SpellEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<SpellEffect> effects) {
        this.effects = effects;
    }
}
