package hu.noroc.gameworld.components.behaviour;


/**
 * Created by Oryk on 3/19/2016.
 */
public interface NPC extends Being, Runnable {

    /* Logic */
    void spawn(long time);

    /* Properties */
    int getExperience();
    String getLoot(Being killer);

}
