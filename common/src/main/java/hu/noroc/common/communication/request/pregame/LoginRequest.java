package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Login request.
 *
 * Created by Oryk on 3/28/2016.
 */
public class LoginRequest extends Request {
    private String username;
    private String password;

    public LoginRequest() {
        this.type = LoginRequest.class.getSimpleName();
    }

    public LoginRequest(String username, String password) {
        this.type = LoginRequest.class.getSimpleName();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
