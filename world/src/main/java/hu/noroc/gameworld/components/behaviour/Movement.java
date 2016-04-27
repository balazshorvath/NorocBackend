package hu.noroc.gameworld.components.behaviour;

/**
 * speed is: speed * 10.0 (pixel) / 100 (ms) - based on the Timer.TICK_UNIT and SPEED_DEFINITION_DISTANCE
 *
 * Created by Oryk on 4/27/2016.
 */
public class Movement {
    public static final double SPEED_DEFINITION_DISTANCE = 10.0;

    public static int calcTime(double xFrom, double yFrom, double xTo, double yTo, double speed){
        double dist = Math.sqrt((xTo - xFrom)*(xTo - xFrom) + (yTo - yFrom)*(yTo - yFrom));
        return (int) (dist / (SPEED_DEFINITION_DISTANCE * speed));
    }
}
