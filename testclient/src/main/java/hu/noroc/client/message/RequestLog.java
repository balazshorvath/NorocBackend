package hu.noroc.client.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.noroc.common.communication.request.Request;
import org.mongojack.ObjectId;

import java.util.Date;

/**
 * Created by Oryk on 4/29/2016.
 */
public class RequestLog {
    private String id;
    private Date sent;
    private Request message;

    public RequestLog() {
    }

    public RequestLog(Date sent, Request message) {
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

    public Request getMessage() {
        return message;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public void setMessage(Request message) {
        this.message = message;
    }
}
