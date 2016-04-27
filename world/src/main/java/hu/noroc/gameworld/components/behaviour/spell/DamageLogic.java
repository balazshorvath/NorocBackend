package hu.noroc.gameworld.components.behaviour.spell;

import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * Spell types represented by this logic:
 *      DAMAGE, HEAL
 * Created by Oryk on 4/27/2016.
 */
public class DamageLogic extends SpellLogic{
    public DamageLogic(SpellEffect spellEffect, String spellName, String spellId, String characterId) {
        super(spellEffect, spellName, spellId, characterId);
    }

    public DamageLogic(SpellLogic logic) {
        super(logic);
    }

    @Override
    public void effect(Being being) {
        switch (type){
            case DAMAGE:
                if(damageType == DamageType.PHYSICAL)
                    being.getStats().health -= (stat.health - being.getStats().armor);
                else if (damageType == DamageType.MAGIC)
                    being.getStats().health -= (stat.health - being.getStats().magicResist);
                break;
            case HEAL:
                being.getStats().health += stat.health;
                break;
        }
    }
}
