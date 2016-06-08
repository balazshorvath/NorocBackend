package hu.noroc.common.communication.message;

/**
 * Created by Oryk on 2016. 05. 28..
 */
public class DataMessage extends Message {
    Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
