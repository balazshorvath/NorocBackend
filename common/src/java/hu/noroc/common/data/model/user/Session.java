package hu.noroc.common.data.model.user;

import java.util.Date;

/**
 * Created by Oryk on 1/11/2016.
 */
public class Session {
    private String sessionId;
    private Date loginDate = new Date();
    private User user;

    public Session() {
    }

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
