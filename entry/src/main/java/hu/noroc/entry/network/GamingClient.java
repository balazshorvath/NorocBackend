package hu.noroc.entry.network;

import hu.noroc.common.communication.request.PauseRequest;
import hu.noroc.common.communication.request.ReconnectRequest;
import hu.noroc.common.communication.request.pregame.ChooseCharacterRequest;
import hu.noroc.common.communication.request.pregame.CreateCharacterRequest;
import hu.noroc.common.communication.request.pregame.DeleteCharacterRequest;
import hu.noroc.common.communication.response.ListCharacterResponse;
import hu.noroc.common.communication.response.ListWorldsResponse;
import hu.noroc.common.communication.response.LoginResponse;
import hu.noroc.common.communication.response.standard.ErrorResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.pregame.LoginRequest;
import hu.noroc.common.communication.response.standard.SuccessResponse;
import hu.noroc.common.data.model.user.User;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.entry.NorocEntry;
import hu.noroc.entry.security.Compressor;
import hu.noroc.gameworld.World;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Oryk on 4/13/2016.
 */
public class GamingClient extends Client implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(NorocEntry.class.getName());
    public GamingClient(Socket socket, String session, User user) {
        super(socket, session, user);
    }

    private World world;

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();
        int fails = 0;
        online = true;
        state = ClientState.CONNECTED;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(Compressor.gzip(mapper.writeValueAsString(new SuccessResponse())) + '\n');
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.severe("Connection problem.");
            state = ClientState.DISCONNECTED;
            online = false;
        }
        Request request;
        String message;
        while(online){
            try {
                message = reader.readLine();
                if(message == null && ++fails > 10) {
                    LOGGER.info("Connection problem.");
                    state = ClientState.DISCONNECTED;
                    break;
                }
            } catch (SocketTimeoutException e) {
                LOGGER.info("Client timeout.");
                state = ClientState.TIMED_OUT;
                break;
            } catch (IOException e) {
                LOGGER.info("Connection problem.");
                state = ClientState.DISCONNECTED;
                break;
            }
            LOGGER.info("Recvd message: " + message);
            try {
                request = mapper.readValue(Compressor.gunzip(message), Request.class);
            } catch (Exception e) {
                LOGGER.info("Invalid message.");
                continue;
            }

            if(super.inGame && !((request instanceof PauseRequest) || (request instanceof ReconnectRequest))){
                try {
                    world.newClientRequest(request, characterId);
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }else{
                SimpleResponse response = preGame(request);
                response = response == null ? new ErrorResponse(SimpleResponse.INVALID_REQUEST) : response;
                //if response code is 0, there was a pause request
                if(response.getCode() == 0)
                    break;
                try {
                    writer.write(Compressor.gzip(mapper.writeValueAsString(response)) + '\n');
                    writer.flush();
                } catch (Exception ignored) {
                }
            }
        }
        disconnect();
        LOGGER.info("Client disconnected. Session lives.");
        if(state == ClientState.CONNECTED) state = ClientState.UNKNOWN;
    }

    private SimpleResponse preGame(Request request){
        switch (request.getType()){
            case "LoginRequest": {
                LoginRequest loginRequest = (LoginRequest) request;
                User user1 = NorocDB.getInstance().getUserRepo().login(loginRequest.getUsername(), loginRequest.getPassword());
                if(user1 == null)
                    return new ErrorResponse(SimpleResponse.LOGIN_FAILED);
                Optional<Client> optional = NorocEntry.clients.values().stream()
                        .filter(client1 ->
                                client1.getUser() != null && user1.getUsername().equals(client1.getUser().getUsername())
                        ).findFirst();
                if(optional.isPresent()){
                    Client c = optional.get();
                    c.disconnect();
                    if(c.getWorldId() != null)
                        NorocEntry.worlds.get(c.worldId).logoutCharacter(c.user.getId(), c.characterId);

                }
                this.user = user1;
                return new LoginResponse(this.session);
            }
            case "ListWorldsRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                return new ListWorldsResponse(
                        NorocEntry.worlds.entrySet().stream().map(
                                world -> new ListWorldsResponse.WorldData(
                                        world.getKey(),
                                        world.getValue().getName(),
                                        world.getValue().getPlayerCount(),
                                        world.getValue().getMaxPlayers()
                                        )
                        ).collect(Collectors.toList())
                );
            case "ListCharactersRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                try {
                    return new ListCharacterResponse(
                            NorocDB.getInstance().getCharacterRepo().findByUser(user.getId())
                    );
                } catch (IOException e) {
                    return new ErrorResponse();
                }
            case "CreateCharacterRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                try {
                    return NorocEntry.createCharacter((CreateCharacterRequest) request, user);
                } catch (IOException e) {
                    return new ErrorResponse();
                }
            case "DeleteCharacterRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                try {
                    return NorocEntry.deleteCharacter((DeleteCharacterRequest) request, user);
                } catch (IOException e) {
                    return new ErrorResponse();
                }
            case "ChooseCharacterRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                world = NorocEntry.worlds.get(((ChooseCharacterRequest)request).getWorldId());
                if(world == null)
                    return new ErrorResponse(SimpleResponse.INVALID_REQUEST, "World server not found!");

                try {
                    characterId = world.loginCharacter((ChooseCharacterRequest) request, user.getId());
                } catch (Exception e) {
                    return new ErrorResponse(SimpleResponse.INTERNAL_ERROR, "Character not found!");
                }
                super.worldId = ((ChooseCharacterRequest) request).getWorldId();
                super.inGame = true;
                return new SuccessResponse();

            case "LogoutCharacterRequest":
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                //TODO
                inGame = false;
                characterId = null;
                worldId = null;
                break;
            case "PauseRequest":
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                if(inGame)
                    state = ClientState.CONNECTING;
                else
                    state = ClientState.PAUSED;
                disconnect();
                return new SuccessResponse(0);
        }
        return null;
    }

    public void reconnect(){
        if(super.inGame && !world.isOnline(characterId)){
            Request request = new ChooseCharacterRequest(this.worldId, this.characterId);
            try {
                world.loginCharacter((ChooseCharacterRequest) request, user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
