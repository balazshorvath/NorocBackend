package hu.noroc.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.noroc.client.message.MessageDatabase;
import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.request.PauseRequest;
import hu.noroc.common.communication.request.ReconnectRequest;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.pregame.ChooseCharacterRequest;
import hu.noroc.common.communication.request.pregame.ListCharactersRequest;
import hu.noroc.common.communication.request.pregame.ListWorldsRequest;
import hu.noroc.common.communication.request.pregame.LoginRequest;
import hu.noroc.common.communication.response.ListCharacterResponse;
import hu.noroc.common.communication.response.ListWorldsResponse;
import hu.noroc.common.communication.response.LoginResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.npc.NPCData;
import org.codehaus.jackson.JsonGenerationException;
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

    private String session;
    private PlayerCharacter current;
    private List<NPCData> npcs;
    private List<PlayerCharacter> players;

    private List<PlayerCharacter> characters;
    private List<ListWorldsResponse.WorldData> worlds;

    private MessageDatabase messageDatabase;

    private Thread listener;


    public SimpleResponse init(String ip, int port) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(!socket.isClosed())
            return null;
        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        mapper = new ObjectMapper();

        return recvResponse();
    }
    public SimpleResponse pauseAndReconnect(String ip, int port) throws IOException {
        Request request = new PauseRequest();

        sendIngameRequest(request);

        socket.close();

        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        request = new ReconnectRequest();

        sendIngameRequest(request);

        return recvResponse();
    }
    public SimpleResponse login(String username, String password) throws Exception {
        if(listener != null && !listener.getState().equals(Thread.State.TERMINATED))
            return null;
        LoginRequest request = new LoginRequest(username, password);
        sendRequest(request);

        SimpleResponse response = recvResponse();

        if(response.getCode() == SimpleResponse.SUCCESS){
            session = ((LoginResponse)response).getSession();
        }
        return response;
    }

    public SimpleResponse listCharacters() throws Exception {
        if(listener != null && !listener.getState().equals(Thread.State.TERMINATED))
            return null;
        ListCharactersRequest request = new ListCharactersRequest();
        request.setSession(session);
        sendRequest(request);

        SimpleResponse response = recvResponse();

        if(response.getCode() == SimpleResponse.SUCCESS){
            characters = ((ListCharacterResponse)response).getCharacters();
        }
        return response;
    }
    public SimpleResponse listWorlds() throws Exception {
        if(listener != null && !listener.getState().equals(Thread.State.TERMINATED))
            return null;
        ListWorldsRequest request = new ListWorldsRequest();
        request.setSession(session);
        sendRequest(request);

        SimpleResponse response = recvResponse();

        if(response.getCode() == SimpleResponse.SUCCESS){
            worlds = ((ListWorldsResponse)response).getWorlds();
        }
        return response;
    }
    public SimpleResponse chooseCharacter(String characterId, String worldId) throws Exception {
        if(listener != null && !listener.getState().equals(Thread.State.TERMINATED))
            return null;
        ChooseCharacterRequest request = new ChooseCharacterRequest(worldId, characterId);
        request.setSession(session);
        sendRequest(request);

        SimpleResponse response = recvResponse();

        if(response.getCode() == SimpleResponse.SUCCESS){
            current = characters.stream()
                    .filter(playerCharacter -> playerCharacter.getId().equals(characterId))
                    .findFirst().get();
        }
        return response;
    }

    public void sendIngameRequest(Request request) throws IOException {
        request.setSession(session);
        sendRequest(request);
    }

    private void sendRequest(Request request) throws IOException {
        writer.write(mapper.writeValueAsString(request) + '\n');
        writer.flush();
        messageDatabase.logOutgoing(request);
    }

    private SimpleResponse recvResponse() throws IOException {
        SimpleResponse response = mapper.readValue(reader.readLine(), SimpleResponse.class);
        messageDatabase.logIncoming(response);
        return response;
    }

    public void startAsyncListening(){
        if(listener != null && !listener.getState().equals(Thread.State.TERMINATED))
            return;
        listener = new Thread(() -> {
            String request;
            try {
                //TODO handle messages
                request = reader.readLine();
                messageDatabase.logIncoming(mapper.readValue(request, Message.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void disconnect() throws IOException {
        if(!socket.isClosed()){
            socket.close();
        }
    }
    public void printVisible(){
        npcs.forEach(npcData -> {
            try {
                System.out.println(mapper.writeValueAsString(npcData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        players.forEach(player -> {
            try {
                System.out.println(mapper.writeValueAsString(player));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void printCharacter(){
        try {
            System.out.println(mapper.writeValueAsString(current));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessageDatabase(MessageDatabase messageDatabase) {
        this.messageDatabase = messageDatabase;
    }
    public MessageDatabase getMessageDatabase() {
        return messageDatabase;
    }

    public List<ListWorldsResponse.WorldData> getWorlds() {
        return worlds;
    }

    public List<PlayerCharacter> getCharacters() {
        return characters;
    }

    public PlayerCharacter getCurrent() {
        return current;
    }

    public String getSession() {
        return session;
    }

    public List<NPCData> getNpcs() {
        return npcs;
    }

}
