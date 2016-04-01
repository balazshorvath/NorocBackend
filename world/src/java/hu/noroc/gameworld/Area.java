package hu.noroc.gameworld;

import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.components.scripting.ScriptedNPC;
import hu.noroc.gameworld.messaging.EventMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Oryk on 3/19/2016.
 */
public class Area {
    private final static Logger LOGGER = Logger.getLogger(Area.class.getName());

    protected World world;
    /* Areas are squares */
    private double sideLength;
    private int mapWidth;
    private static Integer areaId = 0;

    protected Integer id;
    protected double startX, startY;


    protected List<ScriptedNPC> npcs = new ArrayList<>();
    protected List<Player> players = new ArrayList<>();

    protected BlockingDeque<EventMessage> areaMessenger = new LinkedBlockingDeque<>();

    protected Thread messageConsumer;

    public Area(double sideLength, int mapWidth, World world) {
        this.world = world;
        this.id = areaId++;
        this.sideLength = sideLength;
        this.mapWidth = mapWidth;

        int indexHeight = id / mapWidth;
        int indexWidth = id % mapWidth;

        startX = indexHeight * sideLength;
        startY = indexWidth * sideLength;

        messageConsumer = new Thread(() -> {
            EventMessage message = null;
            while(World.isRunning()){
                try {
                    message = areaMessenger.poll(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    continue;
                }
                if(message == null){
                    continue;
                }
                //TODO pass msgs
                //TODO ifPlayer movement & falls out of range ask to delete player
                System.out.println("got a message..." + message);
            }
        });

        messageConsumer.start();

    }

    public void newMessage(EventMessage message){
        areaMessenger.push(message);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isInside(double x, double y) {
        return !(x < startX || y < startY) && !(x > startX + sideLength || y > startY + sideLength);
    }

    public List<ScriptedNPC> getNpcs() {
        return npcs;
    }

    public double getSideLength() {
        return sideLength;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public Integer getId() {
        return id;
    }
}
