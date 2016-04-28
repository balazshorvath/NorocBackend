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
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import hu.noroc.gameworld.messaging.EntityActivityType;
import hu.noroc.gameworld.messaging.Event;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import hu.noroc.gameworld.messaging.sync.SyncMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oryk on 4/3/2016.
 */
public class Player implements Being, LivingEntity {
    private String session;
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private Set<BuffLogic> effects = new HashSet<>();

    private Area area;
    private World world;
    private double viewDist;

    private int nextCast;
    private CharacterSpell casting;
    private int nextWayPoint = 0;
    private int nextWayPointTime;
    private double[][] movement;

    private int tickCount = 0;


    public void update(){
        //TODO: update stats, spells based on items, buffs, debuffs, talents (if there will be such thing)
    }

    @Override
    public void newEvent(Event message) {
        world.newSyncMessage(new SyncMessage(session, message));
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into Event, put into areaMessenger, act as expected
        if(effects.stream().anyMatch(spellEffect -> spellEffect.getType() == SpellEffect.SpellType.STUN))
            return;
        if (request instanceof PlayerAttackRequest){
            PlayerAttackRequest playerAttackRequest = (PlayerAttackRequest) request;

            if((casting = character.getSpells().get(playerAttackRequest.getSpellId())) == null)
                return;
            if(tickCount < nextCast)
                return;
            if(tickCount < casting.getNextCast())
                return;
            nextCast = tickCount + casting.getCastTime();
            casting.setCooldown(nextCast);

            movement = null;
            nextWayPoint = 0;
        }else if (request instanceof PlayerInteractRequest){
            PlayerInteractRequest playerInteractRequest = (PlayerInteractRequest) request;

            //TODO

        }else if (request instanceof PlayerMoveRequest){
            if(casting != null)
                return;
            PlayerMoveRequest playerMoveRequest = (PlayerMoveRequest) request;

            this.movement = playerMoveRequest.getPath();

            DirectionalEvent event = new DirectionalEvent();
            event.setX(movement[0][0]);
            event.setY(movement[0][1]);
            event.setDirectionalType(DirectionalEvent.DirectionalType.MOVING_TO);
            nextWayPoint = 1;
            nextWayPointTime = Movement.calcTime(movement[0][0], movement[0][1], movement[1][0], movement[1][1], stats.speed);
            world.newSyncMessage(new SyncMessage(session, event));
        }else if (request instanceof PlayerEquipRequest){
            PlayerEquipRequest playerEquipRequest = (PlayerEquipRequest) request;

        }else if(request instanceof InitRequest){
            InitRequest initRequest = (InitRequest) request;

        }
    }

    @Override
    public void attacked(SpellLogic logic, Being caster) {
        logic.effect(this);

        AttackEvent event = new AttackEvent();
        event.setActivity(EntityActivityType.ATTACK);
        event.setBeing(caster);
        event.setX(event.getX());
        event.setY(event.getY());

        world.newSyncMessage(new SyncMessage(session, event));
    }

    @Override
    public void tick() {
        if(casting != null && tickCount++ == nextCast){
            casting.createLogics().forEach(spellLogic -> {
                AttackEvent event = new AttackEvent();

                event.setActivity(EntityActivityType.ATTACK);
                event.setBeing(this);
                event.setX(event.getX());
                event.setY(event.getY());
                event.setRadius(casting.getRadius());
                event.setAlpha(casting.getAlpha());

                area.newMessage(event);
            });
            // Send back a smaller message, since it's just an acknowledgement
            AttackEvent event = new AttackEvent();
            event.setActivity(EntityActivityType.ATTACK);
            event.setBeing(this);
            world.newSyncMessage(new SyncMessage(session, event));
            casting = null;
        }
        if(nextWayPoint != 0 && nextWayPointTime == tickCount){
            if(movement.length != nextWayPoint) {
                nextWayPointTime = tickCount + Movement.calcTime(
                        movement[nextWayPoint][0], movement[nextWayPoint][1],
                        movement[nextWayPoint + 1][0], movement[nextWayPoint + 1][1],
                        stats.speed
                );

                DirectionalEvent event = new DirectionalEvent();
                event.setX(movement[nextWayPoint][0]);
                event.setY(movement[nextWayPoint][1]);
                event.setDirectionalType(DirectionalEvent.DirectionalType.CURRENTLY_AT);

                world.newSyncMessage(new SyncMessage(session, event));
                area.newMessage(event);

                event = new DirectionalEvent();
                event.setX(movement[nextWayPoint + 1][0]);
                event.setY(movement[nextWayPoint + 1][1]);
                event.setDirectionalType(DirectionalEvent.DirectionalType.MOVING_TO);

                nextWayPoint++;
            }else{
                DirectionalEvent event = new DirectionalEvent();
                event.setX(movement[nextWayPoint][0]);
                event.setY(movement[nextWayPoint][1]);
                event.setDirectionalType(DirectionalEvent.DirectionalType.CURRENTLY_AT);
                nextWayPoint = 0;
                movement = null;
            }
        }
        effects.forEach(spellEffect -> spellEffect.tick(this));

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
        return area == null ? null : area.getId();
    }

    @Override
    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean isInside(double x, double y) {
        return ((x - this.character.getX())*(x - this.character.getX()) + (y - this.character.getY())*(y - this.character.getY())) < viewDist;
    }

    @Override
    public Set<BuffLogic> getEffects() {
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

    public void setEffects(Set<BuffLogic> effects) {
        this.effects = effects;
    }

    public long getNextCast() {
        return nextCast;
    }

    public void setNextCast(int nextCast) {
        this.nextCast = nextCast;
    }

    public double[][] getMovement() {
        return movement;
    }

    public void setMovement(double[][] movement) {
        this.movement = movement;
    }

}
