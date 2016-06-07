package hu.noroc.gameworld.components.behaviour;

import hu.noroc.common.communication.message.models.PlayerCharacterResponse;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.ingame.*;
import hu.noroc.gameworld.components.behaviour.spell.OverTimeLogic;
import hu.noroc.gameworld.messaging.*;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import hu.noroc.gameworld.messaging.sync.SyncMessage;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Oryk on 4/3/2016.
 */
public class Player implements Being, ActingEntity {
    private String session;
    private PlayerCharacter character;
    private CharacterClass characterClass;
    private CharacterStat stats;
    private int currentHealth;
    private int currentMana;

    private Set<BuffLogic> effects = new CopyOnWriteArraySet<>();
    private boolean dead = true;
    private boolean inited = false;

    private Area area;
    private World world;
    private double viewDist;

    private int nextCast;
    private CharacterSpell casting;
    private double castingX;
    private double castingY;

    private Movement movement;

    static final int tckCountReset = Integer.MAX_VALUE / 2;
    private Long tickCount = Long.valueOf(0);
    private boolean countReset = false;

    public void update(){
        //TODO: update stats, spells based on items, buffs, debuffs, talents (if there will be such thing)
        if(!inited)
            this.stats = new CharacterStat(characterClass.getStat());
        if(dead)
            return;

        this.stats = new CharacterStat(characterClass.getStat());
        effects.forEach(buffLogic -> {
            if(! (buffLogic instanceof OverTimeLogic)){
                this.stats.spirit += buffLogic.getStat().spirit;
                this.stats.strength += buffLogic.getStat().strength;
                this.stats.stamina += buffLogic.getStat().stamina;
                this.stats.intellect += buffLogic.getStat().intellect;
            }
        });
        this.stats.health += (this.stats.stamina * 10);
        this.stats.mana += (this.stats.intellect * 10);
    }

    @Override
    public void newEvent(Event message) {
        if(message.getBeing() != null && message.getBeing().getId().equals(this.getId())
                && !((message instanceof DataEvent) || (message instanceof InitEvent) || (message instanceof AreaChangedEvent)))
            return;
        world.newSyncMessage(new SyncMessage(session, message));
    }

    public void clientRequest(Request request){
        //TODO: validate, transform into Event, put into areaMessenger, act as expected
        if(dead && request instanceof InitRequest){
            update();
            currentHealth = stats.health;
            currentMana = stats.mana;
            InitRequest initRequest = (InitRequest) request;
            InitEvent response = new InitEvent();
            response.setSelf(new InitEvent.InGamePlayer(this));
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            world.newSyncMessage(new SyncMessage(session, response));
            this.dead = false;
            //TODO this is shit
            this.inited = true;
            return;
        }
        if(currentHealth <= 0 && ! (request instanceof RespawnRequest))
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

            Movement.Position p = movement.stop();
            setX(p.x);
            setY(p.y);

            AttackEvent event = new AttackEvent(DirectionalEvent.DirectionalType.CAST);
            event.setBeing(this);
            event.setX(((PlayerAttackRequest) request).getX());
            event.setY(((PlayerAttackRequest) request).getY());
            event.setEffect(casting.getEffect().createLogic(getId(), casting.getId(), casting.getName()));
            event.setActivity(EntityActivityType.ATTACK);
            area.newMessage(event);

            nextCast = (int) (tickCount + casting.getCastTime());
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

            Movement.Position[] path = new Movement.Position[playerMoveRequest.getPath().length - start];
            for (int i = start; i < playerMoveRequest.getPath().length; i++) {
                path[i - start] = new Movement.Position(playerMoveRequest.getPath()[i].getX(), playerMoveRequest.getPath()[i].getY());
            }
            movement.newMovement(path, movement.getCurrentPosition(), this.stats.speed);
            sendMovingTo();
        }else if (request instanceof PlayerEquipRequest){
            PlayerEquipRequest playerEquipRequest = (PlayerEquipRequest) request;

        }else if(request instanceof RespawnRequest){
            if(!dead)
                return;
            this.character.setX(256.0);
            this.character.setY(170.0);

            update();
            currentHealth = stats.health;
            currentMana = stats.mana;
            InitEvent response = new InitEvent();
            response.setSelf(new InitEvent.InGamePlayer(this));
            world.newSyncMessage(new SyncMessage(session, response));
            sendDataEvent();
            this.dead = false;
        }
    }

    @Override
    public void attacked(SpellLogic logic, Being caster) {
        if(this.currentHealth <= 0)
            return;
        logic.effect(this);
    }

    @Override
    public void tick(){
        if(!inited)
            return;
        tickCount++;
        if(tickCount >= tckCountReset
                && (casting == null)
                && (!movement.hasNext())){
            tickCount = 0L;
        }

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

        if(movement.hasNext()){
            Movement.Position p = movement.tick(this);
            if(p != null) {
                this.setX(p.x);
                this.setY(p.y);
            }
//            sendCurrentlyAt();
        }
        //TODO this is bad, the spells wont be able to remove themselfs
        effects.forEach(spellEffect -> spellEffect.tick(this));

        update();

        if(this.currentHealth <= 0) {
            this.dead = true;
            this.currentHealth = 0;
            this.movement.stop();
        }
        if(this.currentHealth > this.characterClass.getStat().health){
            this.currentHealth = this.characterClass.getStat().health;
        }
        if(this.currentMana > this.characterClass.getStat().mana){
            this.currentMana = this.characterClass.getStat().mana;
        }
        if(tickCount % 50 == 0 && !dead){
            this.currentHealth += 5;
            this.currentMana += 5;
        }
        sendDataEvent();

    }

    public void sendMovingTo(){
        Movement.Position p = movement.getNextWayPoint();
        DirectionalEvent event = new DirectionalEvent();
        event.setX(p.x);
        event.setY(p.y);
        event.setBeing(this);
        event.setDirectionalType(DirectionalEvent.DirectionalType.MOVING_TO);

        area.newMessage(event);
    }
//    public void sendCurrentlyAt(){
//        DirectionalEvent event = new DirectionalEvent();
//        event.setX(getX());
//        event.setY(getY());
//        event.setBeing(this);
//        event.setDirectionalType(DirectionalEvent.DirectionalType.CURRENTLY_AT);
//
//        area.newMessage(event);
//        world.newSyncMessage(new SyncMessage(session, event));
//    }

    private void sendDataEvent(){
        area.newMessage(new DataEvent(new PlayerCharacterResponse(
                this.character,
                this.currentHealth,
                this.currentMana,
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
        return character.getId();
    }

    @Override
    public void setId(String id) {
        //do not.
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public void initMovement(double x, double y){
        this.movement = new Movement(new Movement.Position(x, y));
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    @Override
    public int getCurrentMana() {
        return currentMana;
    }

    @Override
    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }
}
