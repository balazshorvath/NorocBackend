package hu.noroc.common.communication.request;

/**
 * Created by Oryk on 2016. 06. 05..
 */
public class PingRequest extends Request {
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
