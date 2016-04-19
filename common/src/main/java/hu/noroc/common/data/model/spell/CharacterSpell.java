package hu.noroc.common.data.model.spell;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by Oryk on 4/14/2016.
 */
public class CharacterSpell extends Spell {
    /* in-game properties */
    private long nextCast;
    /* upgrading */
    private String characterId;
    private int upgradesSpent;
    private SpellEffect[] effects = new SpellEffect[2];

    @JsonIgnore
    public void setCooldown(long now){
        this.nextCast = now  + this.cooldown;
    }

    public long getNextCast() {
        return nextCast;
    }

    public void setNextCast(long nextCast) {
        this.nextCast = nextCast;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public int getUpgradesSpent() {
        return upgradesSpent;
    }

    public void setUpgradesSpent(int upgradesSpent) {
        this.upgradesSpent = upgradesSpent;
    }

    public SpellEffect[] getEffects() {
        return effects;
    }

    public void setEffects(SpellEffect[] effects) {
        this.effects = effects;
    }
}
