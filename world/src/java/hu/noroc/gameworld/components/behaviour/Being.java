package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.data.model.character.CharacterStat;

/**
 * Created by Oryk on 3/31/2016.
 */
public interface Being {
    void damage(int amount);
    void heal(int amount);
    boolean isInside(double x, double y);

    void addBuff(Spell buff);
    String getBuffs();

    CharacterStat getStats();
    void setStats(CharacterStat stats);

    String getName();
    void setName(String name);
    double getX();
    void setX(double x);
    double getY();
    void setY(double y);
    Integer getArea();
    Integer setArea(Integer areaId);
}
