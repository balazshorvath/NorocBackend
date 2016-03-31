package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 3/28/2016.
 */
public class EntityMoveMessage extends Message {
    private double[][] path;

    public EntityMoveMessage() {
        this.type = EntityMoveMessage.class.getSimpleName();
    }
}
