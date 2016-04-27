package hu.noroc.gameworld.components.behaviour.spell;

import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * Spell types represented by this logic:
 *      BUFF, DEBUFF, STUN
 *
 * Created by Oryk on 4/27/2016.
 */
public class BuffLogic extends SpellLogic {
    protected int tickCount;

    public BuffLogic(SpellEffect spellEffect, String spellName, String spellId, String characterId) {
        super(spellEffect, spellName, spellId, characterId);
        tickCount = 0;
    }

    public BuffLogic(BuffLogic buffLogic) {
        super(buffLogic);
    }

    protected void refreshDuration(){
        this.tickCount = 0;
    }

    public void tick(Being being){
        if(++tickCount > duration)
            being.getEffects().remove(this);
    }

    @Override
    public void effect(Being being) {
        if(being.getEffects().contains(this))
            being.getEffects().stream().filter(this::equals).findFirst().get().refreshDuration();
        being.getEffects().add(new BuffLogic(this));
    }

}
