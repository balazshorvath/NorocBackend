package hu.noroc.common.communication.response;

import hu.noroc.common.communication.response.standard.SimpleResponse;

/**
 * Created by Oryk on 4/14/2016.
 */
public class LoginResponse extends SimpleResponse {
    private String session;

    public LoginResponse() {
        super(SUCCESS);
        super.type = "LoginResponse";
    }

    public LoginResponse(String session) {
        super(SUCCESS);
        super.type = "LoginResponse";
        this.session = session;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
