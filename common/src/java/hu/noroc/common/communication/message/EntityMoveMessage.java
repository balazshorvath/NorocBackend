package hu.noroc.common.communication.message;

/**
 * This type of message is sent, whenever an entity (by now the abstraction is called Being)
 * moves somewhere.
 *
 * The message contains the "checkpoints" of the path the Being is moving on.
 *
 * Created by Oryk on 3/28/2016.
 */
public class EntityMoveMessage extends Message {
    private double[][] path;

    public EntityMoveMessage() {
        this.type = EntityMoveMessage.class.getSimpleName();
    }

    public double[][] getPath() {
        return path;
    }

    public void setPath(double[][] path) {
        this.path = path;
    }
}
