package hu.noroc.client.message.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.noroc.common.communication.message.Message;
import org.mongojack.ObjectId;

import java.util.Date;

/**
 * Created by Oryk on 4/29/2016.
 */
public class MessageLog {
    private String id;
    private Date sent;
    private Message message;

    public MessageLog() {
    }

    public MessageLog(Date sent, Message message) {
        this.sent = sent;
        this.message = message;
    }

    @ObjectId
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @ObjectId
    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public Date getSent() {
        return sent;
    }

    public Message getMessage() {
        return message;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
