package hu.noroc.gameworld.messaging.directional;

import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;

/**
 * Created by Oryk on 4/3/2016.
 */
public class AttackEvent extends DirectionalEvent {
    protected SpellLogic effect;

    public AttackEvent() {
        this.directionalType = DirectionalType.ATTACK;
    }

    public SpellLogic getEffect() {
        return effect;
    }

    public void setEffect(SpellLogic effect) {
        this.effect = effect;
    }

}
