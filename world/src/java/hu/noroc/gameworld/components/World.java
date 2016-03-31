package hu.noroc.gameworld.components;

import hu.noroc.gameworld.config.NPCScript;
import hu.noroc.gameworld.config.WorldConfig;
import hu.noroc.gameworld.messaging.player.PlayerEvent;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Oryk on 3/19/2016.
 */
public class World {
    private final static Logger LOGGER = Logger.getLogger(World.class.getName());
    private static boolean running = true;

    private double mapWidth, mapHeight, areaSize;

    HashMap<String, Player> players = new HashMap<>();
    HashMap<Integer, Area> areas = new HashMap<>();

    public World() {
    }

    /* EVENTS FROM OUTSIDE */
    public void newMessage(PlayerEvent message){
        Player pl = players.get(message.getSession());
        if(pl == null)
            return;
        Area area = areas.get(pl.getArea());
        if(area == null)
            putPlayerToArea(pl);
        areas.get(pl.getArea()).newMessage(message);
    }


    public void initWorld(WorldConfig config){
        this.mapWidth = config.getMapWidth();
        this.mapHeight = config.getMapHeight();
        this.areaSize = config.getAreaSize();

        int areaCount = (int)(mapWidth / areaSize) + (int)(mapHeight / areaSize) + 2;

        LOGGER.info("Initializing areas.");
        for(int i = 0; i < areaCount; i++){
            Area area = new Area(areaSize, (int)(mapWidth / areaSize));
            areas.put(area.getId(), area);
            LOGGER.info("Added area " + area.getId());
        }
        LOGGER.info("Initializing areas finished.");

        LOGGER.info("Initializing NPCs.");
        for(NPCScript script : config.getScripts()){
            NPC npc = script.runNPC();

            Area npcArea = areas.values().stream()
                    .filter(area -> area.isInside(npc.getX(), npc.getY()))
                    .findFirst().get();
            npcArea.getNpcs().add(script);
            LOGGER.info("Added NPC " + npc.getName() + " to area " + npcArea.getId());
        }
        LOGGER.info("Initializing NPCs finished.");
    }

    public void putPlayerToArea(Player player){
        Area area = areas.entrySet().stream().filter(integerAreaEntry ->
            integerAreaEntry.getValue().isInside(player.getX(), player.getY())
        ).findFirst().get().getValue();

        if(area == null)
            return;

        if(player.getArea() != null)
            areas.get(player.getArea()).getPlayers().remove(player);

        area.getPlayers().add(player);
        player.setArea(area.id);
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
}
