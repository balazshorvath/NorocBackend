package hu.noroc.gameworld.components.behaviour.spell;

import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * Spell types represented by this logic:
 *      HOT, DOT
 *
 * Created by Oryk on 4/27/2016.
 */
public class OverTimeLogic extends BuffLogic {
    public OverTimeLogic(SpellEffect spellEffect, String spellName, String spellId, String characterId) {
        super(spellEffect, spellName, spellId, characterId);
    }

    public OverTimeLogic(OverTimeLogic overTimeLogic) {
        super(overTimeLogic);
    }


    public void tick(Being being){
        if(++tickCount > duration)
            being.getEffects().remove(this);
        if(tickCount % period == 0) {
            switch (type){
                case DOT:
                    if(damageType == DamageType.PHYSICAL)
                        being.getStats().health -= (stat.health - being.getStats().armor);
                    else if (damageType == DamageType.MAGIC)
                        being.getStats().health -= (stat.health - being.getStats().magicResist);
                    break;
                case HOT:
                    being.getStats().health += stat.health;
                    break;
            }
        }
    }
}
