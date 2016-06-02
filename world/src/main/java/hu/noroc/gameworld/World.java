package hu.noroc.gameworld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.pregame.ChooseCharacterRequest;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.repository.CharacterClassRepo;
import hu.noroc.common.data.repository.CharacterRepo;
import hu.noroc.common.data.repository.SpellRepo;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.gameworld.components.behaviour.NPC;
import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.components.scripting.ScriptedNPC;
import hu.noroc.gameworld.components.scripting.Ticker;
import hu.noroc.gameworld.config.WorldConfig;
import hu.noroc.gameworld.messaging.AreaChangedEvent;
import hu.noroc.gameworld.messaging.DataEvent;
import hu.noroc.gameworld.messaging.sync.SyncMessage;

import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The "game server".
 *
 * Initialization happens through initWorld.
 *
 * Created by Oryk on 3/19/2016.
 */
public class World {
    private final static Logger LOGGER = Logger.getLogger(World.class.getName());
    private static boolean running = true;

    private double mapWidth, mapHeight, areaSize;
    private String name;
    private int maxPlayers = 50;

    private Ticker playerTicker;
    HashMap<String, Player> players = new HashMap<>();
    HashMap<Integer, Area> areas = new HashMap<>();
    HashMap<String, Spell> spells = new HashMap<>();

    private CharacterRepo characterRepo;
    private CharacterClassRepo characterClassRepo;
    private SpellRepo spellRepo;

    /* Outgoing messages */
    private BlockingDeque<SyncMessage> clientMessages = new LinkedBlockingDeque<>();

    private World() {
    }

    /* EVENTS FROM OUTSIDE */
    public void newClientRequest(Request message){
        Player pl = players.get(message.getSession());
        if(pl == null)
            return;
        if(areas.get(pl.getArea()) == null)
            putPlayerToArea(pl);
        pl.clientRequest(message);
    }

    public void newSyncMessage(SyncMessage message){
        clientMessages.addLast(message);
    }

    public SyncMessage getSyncMessage(){
        try {
            return clientMessages.pollFirst(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void loginCharacter(ChooseCharacterRequest request, String userId) throws Exception{
        PlayerCharacter playerCharacter = characterRepo.findById(request.getCharacterId());
        if(!playerCharacter.getUserId().equals(userId))
            throw new Exception("You do not own this character!");
        CharacterClass characterClass = characterClassRepo.findByCode(playerCharacter.getClassId());
        Player player = new Player();

        player.setCharacter(playerCharacter);
        player.setCharacterClass(characterClass);
        player.setId(request.getSession());
        player.setName(playerCharacter.getName());
        player.setWorld(this);
        //TODO
        player.setViewDist(500.0);

        player.update();

        players.put(player.getId(), player);
        playerTicker.subscribe(player);
    }
    public void logoutCharacter(String userId, String session){
        Player player = players.get(session);
        if(player != null && player.currentArea() != null){
            String id = player.getId();
            this.players.remove(player.getId());
            player.currentArea().getPlayers().remove(player);
            player.currentArea().newMessage(new DataEvent(null, id));
            playerTicker.unsubscribe(player);
        }
    }

    public static World initWorld(WorldConfig config){
        World world = new World();
        world.mapWidth = config.getMapWidth();
        world.mapHeight = config.getMapHeight();
        world.areaSize = config.getAreaSize();
        world.name = config.getName();

        world.characterRepo = NorocDB.getInstance().getCharacterRepo();
        world.characterClassRepo = NorocDB.getInstance().getCharacterClassRepo();
        world.spellRepo = NorocDB.getInstance().getSpellRepo();

        int areaCount = (int)(world.mapWidth / world.areaSize) + (int)(world.mapHeight / world.areaSize) + 2;

        LOGGER.info("Initializing areas.");
        for(int i = 0; i < areaCount; i++){
            Area area = new Area(world.areaSize, (int)(world.mapWidth / world.areaSize), world);
            world.areas.put(area.getId(), area);
            LOGGER.info("Added area " + area.getId());
        }
        LOGGER.info("Initializing areas finished.");

        LOGGER.info("Initializing NPCs.");
        for(ScriptedNPC script : config.getScripts()){
            NPC npc = script.runEntity();

            Area npcArea = world.areas.values().stream()
                    .filter(area -> area.isInside(npc.getX(), npc.getY()))
                    .findFirst().get();
            npcArea.getNpcs().add(script);
            npc.setArea(npcArea);
            LOGGER.info("Added NPC " + npc.getName() + " to area " + npcArea.getId());
        }
        LOGGER.info("Initializing NPCs finished.");
        world.playerTicker = new Ticker();
        world.playerTicker.start();

        //TODO: Spells
        return world;
    }

    public Spell getSpell(String spellId){
        return spells.get(spellId);
    }

    public Ticker getPlayerTicker() {
        return playerTicker;
    }

    public void putPlayerToArea(Player player){
        if(players.get(player.getId()) == null)
            return;

        Area area = areas.entrySet().stream().filter(integerAreaEntry ->
            integerAreaEntry.getValue().isInside(player.getX(), player.getY())
        ).findFirst().get().getValue();

        if(area == null)
            return;

        if(player.currentArea() != null)
            player.currentArea().getPlayers().remove(player);

        player.newEvent(new AreaChangedEvent());
        area.newPlayer(player);
        player.setArea(area);
    }

    public int getMaxPlayers(){
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        //TODO: handle player already playing.
        this.maxPlayers = maxPlayers;
    }

    public int getPlayerCount(){
        return this.players.size();
    }

    public static boolean isRunning() {
        return running;
    }

    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
