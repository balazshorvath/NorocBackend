package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import hu.noroc.gameworld.messaging.Event;

import java.util.Set;

/**
 * Created by Oryk on 3/31/2016.
 */
public interface Being {
    boolean isInside(double x, double y);

    void newEvent(Event message);

    void attacked(SpellLogic logic, Being caster);
    Set<BuffLogic> getEffects();

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

    int getCurrentHealth();
    void setCurrentHealth(int currentHealth);
    int getCurrentMana();
    void setCurrentMana(int currentMana);
}
