package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.communication.message.models.PlayerCharacterResponse;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.ingame.*;
import hu.noroc.gameworld.messaging.DataEvent;
import hu.noroc.gameworld.messaging.InitResponse;
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
public class Player implements Being, ActingEntity {
    private String session;
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private Set<BuffLogic> effects = new HashSet<>();
    private boolean dead = true;

    private Area area;
    private World world;
    private double viewDist;

    private int nextCast;
    private CharacterSpell casting;
    private double castingX;
    private double castingY;
    private int nextWayPoint = -1;
    private int nextWayPointTime;
    private double[][] movement;

    private int tickCount = 0;

    public void update(){
        //TODO: update stats, spells based on items, buffs, debuffs, talents (if there will be such thing)
        this.stats = new CharacterStat(characterClass.getStat());
    }

    @Override
    public void newEvent(Event message) {
        if(!message.getBeing().getId().equals(this.getId()))
            world.newSyncMessage(new SyncMessage(session, message));
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into Event, put into areaMessenger, act as expected
        if(dead && request instanceof InitRequest){
            update();
            this.stats = new CharacterStat(characterClass.getStat());
            InitRequest initRequest = (InitRequest) request;
            InitResponse response = new InitResponse();
            response.setSelf(new InitResponse.InGamePlayer(this));
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            world.newSyncMessage(new SyncMessage(session, response));
            this.dead = false;
            return;
        }
        if(stats.health <= 0 && ! (request instanceof RespawnRequest))
            return;
        if(effects.stream().anyMatch(spellEffect -> spellEffect.getType() == SpellEffect.SpellType.STUN))
            return;
        if (request instanceof PlayerAttackRequest){
            PlayerAttackRequest playerAttackRequest = (PlayerAttackRequest) request;

            if((casting = character.getSpells().get(playerAttackRequest.getSpellId())) == null)
                return;
            if(tickCount < nextCast)
                return;
            if(tickCount < casting.nextCast())
                return;

            if(movement != null && nextWayPoint != -1){
                double xo = getX(), yo = getY();
                double xd = movement[nextWayPoint - 1][0], yd = movement[nextWayPoint - 1][1];

                movement = null;
                nextWayPoint = -1;

                long sumTime = Movement.calcTime(xo, yo, xd, yd, getStats().speed);
                long time = nextWayPointTime - tickCount;
                double percent = 1.0;
                if(sumTime != 0)
                    percent = (time / sumTime);
                double dx = (xd - xo) * percent, dy = (yd - yo) * percent;

                setX(xo + dx);
                setY(yo + dy);
            }

            AttackEvent event = new AttackEvent(DirectionalEvent.DirectionalType.CAST);
            event.setBeing(this);
            event.setX(((PlayerAttackRequest) request).getX());
            event.setY(((PlayerAttackRequest) request).getY());
            event.setEffect(casting.getEffect().createLogic(getId(), casting.getId(), casting.getName()));
            event.setActivity(EntityActivityType.ATTACK);
            area.newMessage(event);

            nextCast = tickCount + casting.getCastTime();
            casting.newCooldown(nextCast);

            castingX = ((PlayerAttackRequest) request).getX();
            castingY = ((PlayerAttackRequest) request).getY();
        }else if (request instanceof PlayerInteractRequest){
            PlayerInteractRequest playerInteractRequest = (PlayerInteractRequest) request;

            //TODO

        }else if (request instanceof PlayerMoveRequest){
            if(casting != null)
                casting = null;

            PlayerMoveRequest playerMoveRequest = (PlayerMoveRequest) request;

            if(playerMoveRequest.getPath() == null || playerMoveRequest.getPath().length < 1)
                return;

            int start = 0;
            if(playerMoveRequest.getPath()[0].getX() == getX() && playerMoveRequest.getPath()[0].getY() == getY())
                start = 1;
            this.movement = new double[playerMoveRequest.getPath().length - start][2];

            for (int i = start; i < playerMoveRequest.getPath().length; i++) {
                this.movement[i - start][0] = playerMoveRequest.getPath()[i].getX();
                this.movement[i - start][1] = playerMoveRequest.getPath()[i].getY();
            }
            nextWayPointTime = 0;
            nextWayPoint = 0;
        }else if (request instanceof PlayerEquipRequest){
            PlayerEquipRequest playerEquipRequest = (PlayerEquipRequest) request;

        }else if(request instanceof RespawnRequest){
            if(!dead)
                return;
            this.character.setX(256.0);
            this.character.setY(170.0);

            update();
            InitResponse response = new InitResponse();
            response.setSelf(new InitResponse.InGamePlayer(this));
            world.newSyncMessage(new SyncMessage(session, response));
            sendDataEvent();
            this.dead = false;
        }
    }

    @Override
    public void attacked(SpellLogic logic, Being caster) {
        if(this.stats.health <= 0)
            return;
        logic.effect(this);
        if(this.stats.health <= 0) {
            this.dead = true;
            this.stats.health = 0;
        }
        sendDataEvent();
    }

    @Override
    public void tick(){
        tickCount++;
        if(casting != null && tickCount >= nextCast){
            casting.createLogics().forEach(spellLogic -> {
                AttackEvent event = new AttackEvent(DirectionalEvent.DirectionalType.ATTACK);

                event.setActivity(EntityActivityType.ATTACK);
                event.setBeing(this);
                event.setX(castingX);
                event.setY(castingY);
                event.setRadius(casting.getRadius());
                event.setAlpha(casting.getAlpha());
                event.setEffect(spellLogic);

                area.newMessage(event);
                casting = null;
            });
        }
        if(nextWayPoint != -1 && nextWayPointTime <= tickCount){
            if(nextWayPoint == 0){
                nextWayPointTime = tickCount + Movement.calcTime(
                        getX(), getY(),
                        movement[nextWayPoint][0], movement[nextWayPoint][1],
                        stats.speed
                );
                sendMovingTo();

                nextWayPoint++;
            }else if(movement.length > nextWayPoint) {
                this.setX(movement[nextWayPoint - 1][0]);
                this.setY(movement[nextWayPoint - 1][1]);

                nextWayPointTime = tickCount + Movement.calcTime(
                        getX(), getY(),
                        movement[nextWayPoint][0], movement[nextWayPoint][1],
                        stats.speed
                );

                sendMovingTo();

                nextWayPoint++;
            }else{
                this.setX(movement[nextWayPoint - 1][0]);
                this.setY(movement[nextWayPoint - 1][1]);

                nextWayPoint = -1;
                movement = null;
            }
        }
        effects.forEach(spellEffect -> spellEffect.tick(this));

    }

    private void sendMovingTo(){
        DirectionalEvent event = new DirectionalEvent();
        event.setX(movement[nextWayPoint][0]);
        event.setY(movement[nextWayPoint][1]);
        event.setBeing(this);
        event.setDirectionalType(DirectionalEvent.DirectionalType.MOVING_TO);

        area.newMessage(event);
    }

    private void sendDataEvent(){
        area.newMessage(new DataEvent(new PlayerCharacterResponse(
                this.character,
                this.getCharacterClass().getStat().health,
                this.getCharacterClass().getStat().mana,
                this.stats
        ), getId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return session.equals(player.session);

    }

    @Override
    public int hashCode() {
        return session.hashCode();
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

    public Area currentArea(){
        return area;
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
