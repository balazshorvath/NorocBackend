package hu.noroc.common.data.model.spell;

import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/14/2016.
 */
public class CharacterSpell extends Spell {
    private String ownerId;
    /* in-game properties */
    private int nextCast;
    /* upgrading */
    private int upgradesSpent;
    private List<SpellEffect> effects = new ArrayList<>();

    public CharacterSpell() {
    }

    public CharacterSpell(Spell spell) {
        super(spell);
    }


    @JsonIgnore
    public List<SpellLogic> getLogics(){
        List<SpellLogic> logics = new ArrayList<>();

        logics.add(effect.createLogic(ownerId, id, name));
        effects.forEach(spellEffect -> logics.add(spellEffect.createLogic(ownerId, id, name)));
        return logics;
    }

    @JsonIgnore
    public void setCooldown(int now){
        this.nextCast = now + this.cooldown;
    }

    @JsonIgnore
    public long getNextCast() {
        return nextCast;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
