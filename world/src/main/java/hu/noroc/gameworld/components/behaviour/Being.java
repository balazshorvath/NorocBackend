package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.messaging.EventMessage;

import java.util.List;

/**
 * Created by Oryk on 3/31/2016.
 */
public interface Being {
    void damage(int amount);
    void heal(int amount);
    boolean isInside(double x, double y);

    void newEvent(EventMessage message);

    /* The reason, why the params have SpellEffect, is because otherwise the damage calc for ex. wouldn't be OK */
    void attacked(SpellEffect effect, Being caster);
    List<Spell> getEffects();

    CharacterStat getStats();
    void setStats(CharacterStat stats);

    String getId();
    void setId(String id);

    String getName();
    void setName(String name);
    double getX();
    void setX(double x);
    double getY();
    void setY(double y);
    Integer getArea();
    void setArea(Area area);
}
