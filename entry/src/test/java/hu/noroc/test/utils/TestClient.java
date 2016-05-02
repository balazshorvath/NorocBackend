package hu.noroc.test.utils;

import hu.noroc.common.communication.request.PauseRequest;
import hu.noroc.common.communication.request.ReconnectRequest;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.pregame.ChooseCharacterRequest;
import hu.noroc.common.communication.request.pregame.ListCharactersRequest;
import hu.noroc.common.communication.request.pregame.ListWorldsRequest;
import hu.noroc.common.communication.request.pregame.LoginRequest;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.npc.NPCData;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.List;

/**
 * Created by Oryk on 4/19/2016.
 */
public class TestClient {
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
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String data = reader.readLine();
        mapper = new ObjectMapper();
        return mapper.readValue(data, SimpleResponse.class);
    }

    public SimpleResponse pauseAndReconnect(String ip, int port) throws IOException {
        Request request = new PauseRequest();
        request.setSession(session);
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();

        socket.close();

        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        request = new ReconnectRequest();

        request.setSession(session);
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();

        String data = reader.readLine();
        return mapper.readValue(data, SimpleResponse.class);
    }

    public SimpleResponse login(String username, String password) throws Exception {
        writer.write(mapper.writeValueAsString(new LoginRequest(username, password)) + '\n');
        writer.flush();
        String data = reader.readLine();
        return mapper.readValue(data, SimpleResponse.class);
    }

    public SimpleResponse listCharacters() throws Exception {
        ListCharactersRequest request = new ListCharactersRequest();
        request.setSession(session);
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();
        return mapper.readValue(reader.readLine(), SimpleResponse.class);
    }
    public SimpleResponse listWorlds() throws Exception {
        ListWorldsRequest request = new ListWorldsRequest();
        request.setSession(session);
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();
        return mapper.readValue(reader.readLine(), SimpleResponse.class);
    }
    public SimpleResponse chooseCharacter(String characterId, String worldId) throws Exception {
        ChooseCharacterRequest request = new ChooseCharacterRequest(worldId, characterId);
        request.setSession(session);
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();
        return mapper.readValue(reader.readLine(), SimpleResponse.class);
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
