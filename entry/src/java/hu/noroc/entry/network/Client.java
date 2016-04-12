package hu.noroc.entry.network;

import hu.noroc.common.data.model.user.User;
import hu.noroc.gameworld.components.behaviour.Player;

import java.net.Socket;
import java.security.KeyPair;
import java.util.Objects;

/**
 * Created by Oryk on 3/28/2016.
 */
public class Client {
    private Socket socket;
    private String session;

    private User user;

    private String worldId;
    private String characterId;

    private KeyPair key;

    public Client() {
    }

    public Client(Socket socket, String session, User user) {
        this.socket = socket;
        this.session = session;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(session, client.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWorldId() {
        return worldId;
    }

    public void setWorldId(String worldId) {
        this.worldId = worldId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public KeyPair getKey() {
        return key;
    }

    public void setKey(KeyPair key) {
        this.key = key;
    }
}
