package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.ingame.*;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.messaging.EntityActivityType;
import hu.noroc.gameworld.messaging.EventMessage;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import hu.noroc.gameworld.messaging.sync.SyncMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/3/2016.
 */
public class Player implements Being {
    private String session;
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private List<SpellEffect> effects = new ArrayList<>();

    private Area area;
    private World world;
    private double viewDist;

    private long nextCast;
    private double[][] movement;

    public void update(){
        //TODO: update stats, spells based on items, buffs, debuffs, talents (if there will be such thing)
    }

    @Override
    public void newEvent(EventMessage message) {
        world.newSyncMessage(new SyncMessage(session, message));
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into EventMessage, put into areaMessenger, act as expected
        if(effects.stream().anyMatch(spellEffect -> spellEffect.getType() == SpellEffect.SpellType.STUN))
            return;
        if (request instanceof PlayerAttackRequest){
            PlayerAttackRequest playerAttackRequest = (PlayerAttackRequest) request;
            CharacterSpell spell;
            if((spell = character.getSpells().get(playerAttackRequest.getSpellId())) == null)
                return;
            long current = System.currentTimeMillis();
            if(current < nextCast)
                return;
            if(current < spell.getNextCast())
                return;
            nextCast = current + spell.getCastTime();
            spell.setNextCast(current);

            AttackEvent event = new AttackEvent();
            event.setEffect(spell.getEffect());
            event.setSpell(spell);
            event.setActivity(EntityActivityType.ATTACK);
            event.setBeing(this);
            event.setX(event.getX());
            event.setY(event.getY());

            area.newMessage(event);
            world.newSyncMessage(new SyncMessage(session, event));
        }else if (request instanceof PlayerInteractRequest){
            PlayerInteractRequest playerInteractRequest = (PlayerInteractRequest) request;

            //TODO

        }else if (request instanceof PlayerMoveRequest){
            PlayerMoveRequest playerMoveRequest = (PlayerMoveRequest) request;

            this.movement = playerMoveRequest.getPath();

            DirectionalEvent event = new DirectionalEvent();
            event.setX(playerMoveRequest.getPath()[0][0]);
            event.setY(playerMoveRequest.getPath()[0][1]);
            event.setDirectionalType(DirectionalEvent.DirectionalType.MOVING_TO);
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
        return session;
    }

    @Override
    public void setId(String id) {
        session = id;
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
        return character.getX();
    }

    @Override
    public void setX(double x) {
        this.character.setX(x);
    }

    @Override
    public double getY() {
        return character.getY();
    }

    @Override
    public void setY(double y) {
        this.character.setY(y);
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
        return ((x - this.character.getX())*(x - this.character.getX()) + (y - this.character.getY())*(y - this.character.getY())) < viewDist;
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

    public void setEffects(List<SpellEffect> effects) {
        this.effects = effects;
    }

    public long getNextCast() {
        return nextCast;
    }

    public void setNextCast(long nextCast) {
        this.nextCast = nextCast;
    }

    public double[][] getMovement() {
        return movement;
    }

    public void setMovement(double[][] movement) {
        this.movement = movement;
    }
}
