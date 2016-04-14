package hu.noroc.common.communication.message.standard;

/**
 * Created by Oryk on 4/13/2016.
 */
public class SimpleMessage {
    private int code;
    private String message;

    public SimpleMessage() {
    }

    public SimpleMessage(int code) {
        this.code = code;
    }

    public SimpleMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
