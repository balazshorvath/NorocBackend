package hu.noroc.gameworld.components;

/**
 * Created by Oryk on 3/31/2016.
 */
public interface Being {


    void damage(int amount);
    void heal(int amount);
    boolean isInside(float x, float y);

    void addBuff(Spell buff);
    String getBuffs();
    /**
     * This string can be converted into CharacterStat object.
     *
     * @return JSON
     */
    String getStats();
    String getName();
    void setName(String name);
    float getX();
    void setX(float x);
    float getY();
    void setY(float y);
    Integer getArea();
    Integer setArea(Integer areaId);
}
