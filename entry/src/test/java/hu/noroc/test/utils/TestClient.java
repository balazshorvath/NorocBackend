package hu.noroc.test.utils;

import hu.noroc.common.communication.request.pregame.LoginRequest;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.npc.NPCData;
import hu.noroc.entry.security.SecurityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.List;

/**
 * Created by Oryk on 4/19/2016.
 */
public class TestClient {
    private String serverURL;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private ObjectMapper mapper;

    private KeyPair key;
    private PublicKey serverPublic;

    private String session;
    private PlayerCharacter character;
    private List<NPCData> npcs;

    public SimpleResponse init(String ip, int port) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        socket = new Socket(ip, port);
        generateKey();

        byte[] rsaKey = new byte[162];
        socket.getInputStream().read(rsaKey, 0, 162);

        this.serverPublic = new RSAPublicKeyImpl(rsaKey);
        socket.getOutputStream().write(key.getPublic().getEncoded());
        socket.getOutputStream().flush();

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String data = reader.readLine();
        mapper = new ObjectMapper();
        return mapper.readValue(data, SimpleResponse.class);
    }
    public SimpleResponse login(String username, String password) throws Exception {
        writer.write(SecurityUtils.encrypt(mapper.writeValueAsString(new LoginRequest(username, password)), serverPublic) + "\n");
        writer.flush();
        String data = reader.readLine();
        data = SecurityUtils.decrypt(data, key.getPrivate());
        return mapper.readValue(data, SimpleResponse.class);
    }

    private void generateKey() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        key = keyGen.generateKeyPair();
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public PlayerCharacter getCharacter() {
        return character;
    }

    public void setCharacter(PlayerCharacter character) {
        this.character = character;
    }

    public List<NPCData> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<NPCData> npcs) {
        this.npcs = npcs;
    }
}
