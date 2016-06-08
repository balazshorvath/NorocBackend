package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.DataMessage;
import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.message.models.PlayerCharacterResponse;

/**
 * Created by Oryk on 2016. 05. 28..
 */
public class DataEvent extends Event {
    PlayerCharacterResponse data;
    String id;

    public DataEvent(PlayerCharacterResponse data, String id) {
        super();
        this.data = data;
        this.id = id;
    }

    public DataEvent() {
        super();
    }

    @Override
    public Message createMessage() {
        DataMessage message = new DataMessage();
        message.setTimestamp(timestamp);
        message.setData(data);
        message.setEntityId(id);
        return message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayerCharacterResponse getData() {
        return data;
    }

    public void setData(PlayerCharacterResponse data) {
        this.data = data;
    }
}
