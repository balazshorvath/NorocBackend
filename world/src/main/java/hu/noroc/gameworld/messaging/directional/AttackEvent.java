package hu.noroc.gameworld.messaging.directional;

import hu.noroc.common.communication.message.AttackMessage;
import hu.noroc.common.communication.message.Message;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;

/**
 * Created by Oryk on 4/3/2016.
 */
public class AttackEvent extends DirectionalEvent {
    protected SpellLogic effect;
    protected double radius;
    protected double alpha;

    public AttackEvent() {
        super();
    }

    @Override
    public Message createMessage() {
        AttackMessage message = new AttackMessage();

        message.setTimestamp(timestamp);

        message.setDirectionalType(directionalType);
        message.setX(x);
        message.setY(y);
        message.setEntityId(being.getId());
        message.setEntityType(entity);

        message.setSpellId(effect.getSpellId());

        return message;
    }

    public AttackEvent(DirectionalType directionalType) {
        if(directionalType != DirectionalType.ATTACK && directionalType != DirectionalType.CAST)
            this.directionalType = DirectionalType.ATTACK;
        this.directionalType = directionalType;
    }

    public SpellLogic getEffect() {
        return effect;
    }

    public void setEffect(SpellLogic effect) {
        this.effect = effect;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
