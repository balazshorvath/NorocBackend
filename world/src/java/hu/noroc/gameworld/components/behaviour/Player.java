package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.communication.request.Request;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.messaging.EventMessage;

import java.util.List;

/**
 * Created by Oryk on 4/3/2016.
 */
public class Player implements Being {
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private List<Spell> effects;

    private double x, y;
    private Area area;
    private World world;
    private double viewDist;



    @Override
    public void newEvent(EventMessage message) {
        //TODO
        //world.messageClient(new SyncMessage());
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into EventMessage, put into areaMessenger, act as expected
    }

    @Override
    public void attacked(SpellEffect effect, Being caster) {
        //TODO: subtype, send event to client
        switch (effect.getMainType()){
            case ATTACK:
                CharacterStat damageStats = effect.getMainStats();
                if(!(caster instanceof Player)){
                    if (effect.getMainDamageType() == SpellEffect.DamageType.PHYSICAL)
                        this.stats.health -= damageStats.health -this.stats.armor;
                    else if (effect.getMainDamageType() == SpellEffect.DamageType.MAGIC)
                        this.stats.health -= damageStats.health - this.stats.magicResist;
                }else if(effect.isMixed()){
                    // This is not the proper, or final way to do it.
                    this.stats.health += damageStats.health;
                }
                break;
            case HEAL:
                break;
            case BUFF:
                break;
            case DEBUFF:
                break;
            case DOT:
                break;
            case HOT:
                break;
        }
    }

    public PlayerCharacter getCharacter() {
        return character;
    }

    public void setCharacter(PlayerCharacter character) {
        this.character = character;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public CharacterStat getStats() {
        return stats;
    }

    public void setStats(CharacterStat stats) {
        this.stats = stats;
    }

    @Override
    public String getId() {
        return character.getId();
    }

    @Override
    public void setId(String id) {
        character.setId(id);
    }

    @Override
    public String getName() {
        return character.getName();
    }

    @Override
    public void setName(String name) {
        //should not happen in this context
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public Integer getArea() {
        return area.getId();
    }

    @Override
    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public void damage(int amount) {
        this.stats.health = this.stats.health - amount;
    }

    @Override
    public void heal(int amount) {
        this.stats.health = this.stats.health + amount;
    }

    @Override
    public boolean isInside(double x, double y) {
        return ((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y)) < viewDist;
    }

    @Override
    public List<Spell> getEffects() {
        return effects;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getViewDist() {
        return viewDist;
    }

    public void setViewDist(double viewDist) {
        this.viewDist = viewDist;
    }
}
