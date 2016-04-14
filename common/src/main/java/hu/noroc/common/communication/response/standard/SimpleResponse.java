package hu.noroc.common.communication.response.standard;

/**
 * Created by Oryk on 4/13/2016.
 */
public class SimpleResponse {
    public static final int LOGIN_FAILED = 210;
    public static final int INTERNAL_ERROR = 200;
    public static final int INVALID_REQUEST = 201;
    public static final int NOT_AUTHENTICATED_ERROR = 211;
    public static final int SUCCESS = 100;

    private int code;
    private String message;

    public SimpleResponse() {
    }

    public SimpleResponse(int code) {
        this.code = code;
    }

    public SimpleResponse(int code, String message) {
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
