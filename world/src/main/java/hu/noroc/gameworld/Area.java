package hu.noroc.gameworld;

import hu.noroc.common.communication.message.EntityType;
import hu.noroc.common.communication.message.models.PlayerCharacterResponse;
import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.components.scripting.ScriptedNPC;
import hu.noroc.gameworld.messaging.AreaChangedEvent;
import hu.noroc.gameworld.messaging.DataEvent;
import hu.noroc.gameworld.messaging.Event;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import hu.noroc.gameworld.messaging.sync.SyncMessage;

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
    protected World world;
    /* Areas are squares */
    private double sideLength;
    private int mapWidth;
    private static Integer areaId = 0;

    protected Integer id;
    protected double startX, startY;


    protected List<ScriptedNPC> npcs = new ArrayList<>();
    protected List<Player> players = new ArrayList<>();

    protected BlockingDeque<Event> areaMessenger = new LinkedBlockingDeque<>();

    protected Thread messageConsumer;

    public Area(double sideLength, int mapWidth, World world) {
        this.world = world;
        this.id = areaId++;
        this.sideLength = sideLength;
        this.mapWidth = mapWidth;

        //TODO: this blocks the possibility of creating multiple world servers
        int indexHeight = id / mapWidth;
        int indexWidth = id % mapWidth;

        startX = indexHeight * sideLength;
        startY = indexWidth * sideLength;

        messageConsumer = new Thread(() -> {
            Event message = null;
            while(World.isRunning()){
                try {
                    message = areaMessenger.pollFirst(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    continue;
                }
                if(message == null){
                    continue;
                }
                if(message instanceof AttackEvent
                        && ((AttackEvent)message).getDirectionalType().equals(DirectionalEvent.DirectionalType.ATTACK)){
                    applySpell((AttackEvent) message);
                }else if(message instanceof DataEvent){
                    final Event finalMessage = message;
                    players.forEach(player -> player.newEvent(finalMessage));
                }else {
                    final Event finalMessage = message;
                    players.forEach(player -> {
                        if (player.isInside(finalMessage.getBeing().getX(), finalMessage.getBeing().getY()))
                            player.newEvent(finalMessage);
                    });
                    npcs.forEach(scriptedNPC -> {
                        if (scriptedNPC.isInside(finalMessage.getBeing().getX(), finalMessage.getBeing().getY()))
                            scriptedNPC.newEvent(finalMessage);
                    });
                    if(message instanceof DirectionalEvent && finalMessage.getEntity() == EntityType.PLAYER
                            && ((DirectionalEvent)message).getDirectionalType() == DirectionalEvent.DirectionalType.MOVING_TO
                            && !isInside(((DirectionalEvent)message).getX(), ((DirectionalEvent)message).getY())){
                        world.putPlayerToArea((Player) message.getBeing());
                    }
                }
            }
        });

        messageConsumer.start();

    }

    private void applySpell(AttackEvent event) {
        final double xp = event.getBeing().getX();
        final double yp = event.getBeing().getY();

        final double xd = event.getX();
        final double yd = event.getY();
        final double r = event.getRadius();
        final double as = Math.toRadians(event.getAlpha());
        // Direction deg
        final double xdp = (xd - xp);
        final double ydp = (yd - yp);
        double ad = Math.atan(ydp / xdp);

        if(xdp < 0) {
            ad += Math.PI;
        }else if(ydp < 0){
            ad += Math.PI * 2;
        }

        //TODO: Consider parallel execution (search->applySpell), while the entity calcs the damage
        // npcs.parallelStream();

        //TODO: Friendly/Unfriendly stuff
        //TODO: block movement/copy entities
        final double finalAd = ad;
        npcs.forEach(scriptedNPC -> {
            // Target - Entity
            double xpt = scriptedNPC.getEntity().getX() - xp;
            double ypt = scriptedNPC.getEntity().getY() - yp;
            double absVecpt = Math.sqrt((xpt*xpt + ypt*ypt));

            if (absVecpt > r)
                return;
            if (as == 180.0) {
                scriptedNPC.getEntity().attacked(event.getEffect(), event.getBeing());
                return;
            }

            double apt = Math.acos(xpt / absVecpt);

            if ((apt <= (finalAd + as)) && ((finalAd - as) <= apt))
                scriptedNPC.getEntity().attacked(event.getEffect(), event.getBeing());
        });
        players.forEach(player -> {
            // Target - Entity
            double xpt = player.getX() - xp;
            double ypt = player.getY() - yp;

            if(xpt == 0 && ypt == 0)
                player.attacked(event.getEffect(), event.getBeing());

            double absVecpt = Math.sqrt((xpt * xpt + ypt * ypt));

            if (absVecpt > r)
                return;
            if (as == 180.0) {
                player.attacked(event.getEffect(), event.getBeing());
                return;
            }
            double apt = Math.atan(ypt / xpt);
            if(xpt < 0) {
                apt += Math.PI;
            }else if(ypt < 0){
                apt += Math.PI * 2;
            }

            if ((apt <= (finalAd + as)) && ((finalAd - as) <= apt))
                player.attacked(event.getEffect(), event.getBeing());
        });

    }

    public void newPlayer(Player player){
        this.players.forEach(player1 -> player.newEvent(new DataEvent(new PlayerCharacterResponse(
                player1.getCharacter(), player1.getCharacterClass().getStat().health, player1.getCharacterClass().getStat().mana,
                player1.getStats()
        ), player1.getId())));
        this.newMessage(new DataEvent(new PlayerCharacterResponse(
                player.getCharacter(), player.getCharacterClass().getStat().health,
                player.getCharacterClass().getStat().mana,
                player.getStats()
        ), player.getId()));
        this.players.add(player);
    }

    public void newMessage(Event message){
        areaMessenger.addLast(message);
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
