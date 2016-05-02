package hu.noroc.common.communication.response.standard;

import hu.noroc.common.communication.response.ListCharacterResponse;
import hu.noroc.common.communication.response.ListWorldsResponse;
import hu.noroc.common.communication.response.LoginResponse;
import hu.noroc.common.communication.response.InitResponse;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Created by Oryk on 4/13/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InitResponse.class, name = "InitResponse"),
        @JsonSubTypes.Type(value = ListCharacterResponse.class, name = "ListCharacterResponse"),
        @JsonSubTypes.Type(value = ListWorldsResponse.class, name = "ListWorldsResponse"),
        @JsonSubTypes.Type(value = LoginResponse.class, name = "LoginResponse"),
        /* Empty responses */
        @JsonSubTypes.Type(value = SuccessResponse.class, name = "SuccessResponse"),
        @JsonSubTypes.Type(value = ErrorResponse.class, name = "ErrorResponse")
})
public abstract class SimpleResponse {
    public static final int INTERNAL_ERROR = 200;
    public static final int INVALID_REQUEST = 201;
    public static final int LOGIN_FAILED = 210;
    public static final int NOT_AUTHENTICATED_ERROR = 211;
    public static final int SUCCESS = 100;

    protected int code;
    protected String type;
    protected String message;

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
