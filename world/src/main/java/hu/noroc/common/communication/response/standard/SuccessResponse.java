package hu.noroc.common.communication.response.standard;

/**
 * Created by Oryk on 4/19/2016.
 */
public class SuccessResponse extends SimpleResponse {
    public SuccessResponse() {
        super(SUCCESS);
    }

    public SuccessResponse(int code) {
        super(code);
    }

    public SuccessResponse(String message) {
        super(SUCCESS, message);
    }
    public SuccessResponse(int code, String message) {
        super(code, message);
    }
}
