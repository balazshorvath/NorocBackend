package hu.noroc.entry.network;

import hu.noroc.common.communication.request.pregame.ChooseCharacterRequest;
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
import hu.noroc.entry.security.SecurityUtils;
import hu.noroc.gameworld.World;
import org.codehaus.jackson.map.ObjectMapper;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.*;
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

    public void initRSA(){
        byte[] buffer = new byte[140];
        try {
            SecurityUtils.generateKey(this);
//            SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(this.key.getPublic().getEncoded());
//            socket.getOutputStream().write(spkInfo.parsePublicKey().getEncoded());
            socket.getInputStream().read(buffer, 0, 140);

            this.clientPublic = new RSAPublicKeyImpl(buffer);
            this.online = true;
        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchProviderException e) {
            online = false;
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();
//        initRSA();
        online = true;
        state = ClientState.CONNECTED;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(mapper.writeValueAsString(new SuccessResponse()) + '\n');
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
            } catch (SocketTimeoutException e) {
                LOGGER.info("Client timeout.");
                state = ClientState.TIMED_OUT;
                break;
            } catch (IOException e) {
                LOGGER.info("Connection problem.");
                state = ClientState.DISCONNECTED;
                break;
            }
//            message = NetworkData.rsaDecryptData(message, this.key.getPrivate());
            LOGGER.info("Recvd message: " + message);
            try {
                request = mapper.readValue(message, Request.class);
            } catch (Exception e) {
                LOGGER.info("Invalid message.");
                continue;
            }

            if(characterId != null && !"".equals(characterId)){
                NorocEntry.worlds.get(this.worldId).newClientRequest(request);
            }else{
                SimpleResponse response = preGame(request);
                response = response == null ? new ErrorResponse(SimpleResponse.INVALID_REQUEST) : response;
                if(response.getCode() == 0)
                    continue;
                try {
                    writer.write(mapper.writeValueAsString(response) + '\n');
//                    writer.write(NetworkData.rsaData(
//                            mapper.writeValueAsString(response),
//                            clientPublic
//                    ));
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
                user = NorocDB.getInstance().getUserRepo().login(loginRequest.getUsername(), loginRequest.getPassword());
                if(user == null)
                    return new ErrorResponse(SimpleResponse.LOGIN_FAILED);
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
                                        world.getValue().getMaxPlayers(),
                                        world.getValue().getPlayerCount()
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
            case "ChooseCharacterRequest":
                if(user == null)
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "You need to login first!");
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                World world = NorocEntry.worlds.get(((ChooseCharacterRequest)request).getWorldId());
                if(world == null)
                    return new ErrorResponse(SimpleResponse.INVALID_REQUEST, "World server not found!");

                try {
                    world.loginCharacter((ChooseCharacterRequest) request, user.getId());
                } catch (Exception e) {
                    return new ErrorResponse(SimpleResponse.INTERNAL_ERROR, "Character not found!");
                }
                return new SuccessResponse();

            case "LogoutCharacterRequest":
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                //TODO
                break;
            case "CreateCharacterRequest":
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                //TODO
                break;
            case "PauseConnection":
                if(!session.equals(request.getSession()))
                    return new ErrorResponse(SimpleResponse.NOT_AUTHENTICATED_ERROR, "Bad session.");
                state = ClientState.PAUSED;
                disconnect();
                return new SuccessResponse(0);
        }
        return null;
    }
}
