package hu.noroc.common.communication.response.standard;

/**
 * Created by Oryk on 4/19/2016.
 */
public class ErrorResponse extends SimpleResponse {
    public ErrorResponse() {
        super(INTERNAL_ERROR);
        super.type = "ErrorResponse";
    }

    public ErrorResponse(int code) {
        super(code);
        super.type = "ErrorResponse";
    }

    public ErrorResponse(String message) {
        super(INTERNAL_ERROR, message);
        super.type = "ErrorResponse";
    }
    public ErrorResponse(int code, String message) {
        super(code, message);
        super.type = "ErrorResponse";
    }
}
