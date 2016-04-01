package hu.noroc.gameworld.components.behaviour;

import hu.noroc.gameworld.messaging.EventMessage;

/**
 * Created by Oryk on 3/19/2016.
 */
public interface NPC extends Being, Runnable {

    /* Logic */
    void newMessage(EventMessage message);
    void spawn(long time);

    /* Properties */
    int getExperience();
    String getLoot(Being killer);

}
