package hu.noroc.gameworld.messaging.directional;

import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.spell.SpellEffect;

/**
 * Created by Oryk on 4/3/2016.
 */
public class AttackEvent extends DirectionalEvent {
    protected SpellEffect effect;
    protected Spell spell;

    public AttackEvent() {
        this.directionalType = DirectionalType.ATTACK;
    }

    public SpellEffect getEffect() {
        return effect;
    }

    public void setEffect(SpellEffect effect) {
        this.effect = effect;
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }
}
