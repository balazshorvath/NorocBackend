package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.ingame.*;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.messaging.EventMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oryk on 4/3/2016.
 */
public class Player implements Being {
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private List<SpellEffect> effects = new ArrayList<>();

    private double x, y;
    private Area area;
    private World world;
    private double viewDist;

    private long nextCast;

    public void run() {
        //TODO: listen for buff durations, spell cast times
    }

    public void update(){
        //TODO: update stats, spells based on items, buffs, debuffs, talents (if there will be such thing)
    }

    @Override
    public void newEvent(EventMessage message) {
        //TODO
        //world.messageClient(new SyncMessage());
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into EventMessage, put into areaMessenger, act as expected
        if(effects.stream().anyMatch(spellEffect -> spellEffect.getType() == SpellEffect.SpellType.STUN))
            return;
        if (request instanceof PlayerAttackRequest){
            PlayerAttackRequest playerAttackRequest = (PlayerAttackRequest) request;
            if(!character.getSpells().containsKey(playerAttackRequest.getSpellId()))
                return;
            long current = System.currentTimeMillis();
            if(current < nextCast)
                return;
            CharacterSpell spell = character.getSpells().get(playerAttackRequest.getSpellId());
            nextCast = current + spell.getCastTime();

        }else if (request instanceof PlayerInteractRequest){
            PlayerInteractRequest playerInteractRequest = (PlayerInteractRequest) request;

        }else if (request instanceof PlayerMoveRequest){
            PlayerMoveRequest playerMoveRequest = (PlayerMoveRequest) request;

        }else if (request instanceof PlayerEquipRequest){
            PlayerEquipRequest playerEquipRequest = (PlayerEquipRequest) request;

        }else if(request instanceof InitRequest){
            InitRequest initRequest = (InitRequest) request;

        }
    }

    @Override
    public void attacked(SpellEffect effect, Being caster) {
        //TODO: subtype, send event to client
        switch (effect.getType()){
            case DAMAGE:
                CharacterStat damageStats = effect.getStat();
                if(!(caster instanceof Player)){
                    if (effect.getDamageType() == SpellEffect.DamageType.PHYSICAL)
                        this.stats.health -= damageStats.health -this.stats.armor;
                    else if (effect.getDamageType() == SpellEffect.DamageType.MAGIC)
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
            case STUN:
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
    public List<SpellEffect> getEffects() {
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
