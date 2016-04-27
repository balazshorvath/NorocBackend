package hu.noroc.common.communication.response.standard;

/**
 * Created by Oryk on 4/19/2016.
 */
public class SuccessResponse extends SimpleResponse {
    public SuccessResponse() {
        super(SUCCESS);
        super.type = "SuccessResponse";
    }

    public SuccessResponse(int code) {
        super(code);
        super.type = "SuccessResponse";
    }

    public SuccessResponse(String message) {
        super(SUCCESS, message);
        super.type = "SuccessResponse";
    }
    public SuccessResponse(int code, String message) {
        super(code, message);
        super.type = "SuccessResponse";
    }
}
