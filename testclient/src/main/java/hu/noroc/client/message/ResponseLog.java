package hu.noroc.client.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import org.mongojack.ObjectId;

import java.util.Date;

/**
 * Created by Oryk on 4/29/2016.
 */
public class ResponseLog {
    private String id;
    private Date received;
    private SimpleResponse message;

    public ResponseLog() {
    }

    public ResponseLog(Date received, SimpleResponse message) {
        this.received = received;
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
    public Date getReceived() {
        return received;
    }

    public SimpleResponse getMessage() {
        return message;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public void setMessage(SimpleResponse message) {
        this.message = message;
    }
}
