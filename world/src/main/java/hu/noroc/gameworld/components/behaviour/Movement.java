package hu.noroc.gameworld.components.behaviour;

/**
 * speed is: speed * 10.0 (pixel) / 100 (ms) - based on the Timer.TICK_UNIT and SPEED_DEFINITION_DISTANCE
 *
 * Created by Oryk on 4/27/2016.
 */
public class Movement {
    private static final double SPEED_DEFINITION_DISTANCE = 10.0;

    private Position[] path;
    private int currentWayPoint;
    private double speed;

    private Position currentPosition;


    public Movement(Position pos) {
        path = new Position[1];
        currentPosition = new Position(pos.x, pos.y);
        path[0] = new Position(pos.x, pos.y);
    }

    public Position newMovement(Position[] path, Position currentPosition, double speed){
        Position p = stop();
        this.path = path;
        this.currentPosition = currentPosition;
        this.speed = speed;
        this.currentWayPoint = 0;
        return p;
    }

    static int calcTime(double xFrom, double yFrom, double xTo, double yTo, double speed){
        double dist = Math.sqrt((xTo - xFrom)*(xTo - xFrom) + (yTo - yFrom)*(yTo - yFrom));
        return (int) (dist / (SPEED_DEFINITION_DISTANCE * speed));
    }

    public Position getCurrentPosition() {
        return new Position(currentPosition.x, currentPosition.y);
    }

    public Position getNextWayPoint() {
        if(hasNext()){
            return new Position(path[currentWayPoint].x, path[currentWayPoint].y);
        }
        return new Position(currentPosition.x, currentPosition.y);
    }

    public Position stop(){
        this.currentWayPoint = path.length;
        return new Position(currentPosition.x, currentPosition.y);
    }
    public boolean hasNext(){
        return currentWayPoint < path.length;
    }

    public Position tick(Player p){
        if(!hasNext())
            return null;
        double xd = path[currentWayPoint].x;
        double yd = path[currentWayPoint].y;

        long sumTime = Movement.calcTime(currentPosition.x, currentPosition.y, xd, yd, speed);
        if(sumTime <= 1) {
            currentPosition.x = path[currentWayPoint].x;
            currentPosition.y = path[currentWayPoint].y;

            currentWayPoint++;
            p.sendMovingTo();
            if(currentWayPoint > path.length) {
                return null;
            }
        }
        double percent = 1.0;
        if(sumTime != 0)
            percent = ((1.0) / (double)sumTime);
        double dx = (xd - currentPosition.x) * percent, dy = (yd - currentPosition.y) * percent;

        currentPosition.x += dx;
        currentPosition.y += dy;

        return new Position(currentPosition.x, currentPosition.y);

    }


    public static class Position{
        public double x, y;

        public Position() {
        }

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
