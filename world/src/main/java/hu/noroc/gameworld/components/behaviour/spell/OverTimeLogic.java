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

    @Override
    public void effect(Being being) {
        if(being.getId().equals(super.characterId) && type.equals(SpellType.DOT))
            return;
        if(!being.getId().equals(super.characterId) && type.equals(SpellType.HOT))
            return;

        if (being.getEffects().contains(this))
            being.getEffects().stream().filter(this::equals).findFirst().get().refreshDuration();
        being.getEffects().add(new OverTimeLogic(this));
    }

    public void tick(Being being){
        if(++tickCount > duration)
            being.getEffects().remove(this);
        if(tickCount % period == 0) {
            switch (type){
                case DOT:
                    if (damageType == DamageType.PHYSICAL)
                        being.setCurrentHealth(being.getCurrentHealth() - (stat.health - being.getStats().armor + being.getStats().strength));
                    else if (damageType == DamageType.MAGIC)
                        being.setCurrentHealth(being.getCurrentHealth() - (stat.health - being.getStats().magicResist + being.getStats().spirit));

                    break;
                case HOT:
                    being.setCurrentHealth(being.getCurrentHealth() + stat.health);
                    break;
            }
        }
    }
}
