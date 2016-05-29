package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 2016. 05. 29..
 */
public class ClearMessage extends Message {

    public ClearMessage() {
        this.type = ClearMessage.class.getSimpleName();
    }
}
