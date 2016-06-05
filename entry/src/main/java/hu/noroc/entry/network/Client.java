package hu.noroc.entry.network;

import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.request.PingRequest;
import hu.noroc.common.communication.response.standard.PingResponse;
import hu.noroc.common.data.model.user.User;
import hu.noroc.entry.NorocEntry;
import hu.noroc.entry.security.Compressor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Oryk on 3/28/2016.
 */
public class Client {
    protected boolean online;
    protected ClientState state;
    protected Socket socket;
    protected String session;

    protected InetAddress clientAddress;
    protected int clientPort;
    protected DatagramSocket datagramSocket;
    protected Long lastPing;

    protected User user;

    protected boolean inGame = false;
    protected String worldId;
    protected String characterId;

    protected KeyPair key;
    protected PublicKey clientPublic;

    public Client() {
    }

    public Client(Socket socket, String session, User user) {
        this.socket = socket;
        this.session = session;
        this.user = user;
    }

    public void disconnect() {
        this.online = false;
        if(this.socket != null && !this.socket.isClosed()){
            try {
                this.socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void forceDisconnect() {
        try {
            this.socket.close();
        } catch (IOException ignore) {
        }
    }

    public void udpPing(PingRequest request, InetAddress address, int port) {
        if(!address.equals(this.clientAddress) || this.clientPort != port) {
            this.clientAddress = address;
            this.clientPort = port;
        }
        this.lastPing = System.currentTimeMillis();
        String mess;
        try {
            mess = (new ObjectMapper().writeValueAsString(new PingResponse(request.getTimestamp(), this.lastPing, 0)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(mess == null) return;
        byte[] bytes;
        try {
            bytes = Compressor.gzip(mess).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, clientAddress, clientPort);
        try {
            NorocEntry.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendSync(Message message){
        if(System.currentTimeMillis() - lastPing < 5000){
            String mess;
            try {
                mess = (new ObjectMapper().writeValueAsString(message));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if(mess == null) return false;
            byte[] bytes;
            try {
                bytes = Compressor.gzip(mess).getBytes();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, clientAddress, clientPort);
            try {
                NorocEntry.socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
    public enum ClientState{
        PAUSED,
        DISCONNECTED,
        TIMED_OUT,
        CONNECTED,
        CONNECTING,
        UNKNOWN
    }

    public ClientState getState() {
        return state;
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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public PublicKey getClientPublic() {
        return clientPublic;
    }

    public void setClientPublic(PublicKey clientPublic) {
        this.clientPublic = clientPublic;
    }

    public void setState(ClientState state) {
        this.state = state;
    }
}
