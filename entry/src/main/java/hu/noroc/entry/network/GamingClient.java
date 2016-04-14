package hu.noroc.entry.network;

import hu.noroc.common.communication.message.standard.SimpleMessage;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.data.model.user.User;
import hu.noroc.entry.NetworkData;
import hu.noroc.entry.NorocEntry;
import hu.noroc.entry.security.SecurityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Oryk on 4/13/2016.
 */
public class GamingClient extends Client implements Runnable {
    public GamingClient(Socket socket, String session, User user) {
        super(socket, session, user);
    }

    public GamingClient() {
    }
    public void initRSA(){
        byte[] buffer = new byte[1024];
        try {
            SecurityUtils.generateKey(this);
            socket.getOutputStream().write(this.key.getPublic().getEncoded());
            int bytes = socket.getInputStream().read(buffer, 0, 1024);
            if(bytes != 1024) {
                online = false;
                return;
            }

            this.clientPublic = new RSAPublicKeyImpl(buffer);
            this.online = true;
        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            online = false;
        }
    }

    @Override
    public void run() {
        BufferedReader reader;
        ObjectMapper mapper = new ObjectMapper();
        initRSA();
        if(!online)
            return;
        try {
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())).write(
                    mapper.writeValueAsString(new SimpleMessage(100))
            );
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            return;
        }
        Request request;
        String message;
        while(online){
            try {
                message = reader.readLine();
            } catch (IOException e) {
                return;
            }
            message = NetworkData.rsaDecryptData(message, this.key.getPrivate());
            try {
                request = mapper.readValue(message, Request.class);
            } catch (IOException e) {
                continue;
            }

            if(!"".equals(characterId)){
                NorocEntry.worlds.get(this.worldId).newClientRequest(request);
            }else{
                preGame(request);
            }
        }

        try {
            this.socket.close();
        } catch (IOException ignored) {
        }
    }
    private void preGame(Request request){
        switch (request.getType()){
            case "LoginRequest":
                break;
            case "ListWorldsRequest":
                break;
            case "ListCharactersRequest":
                break;
            case "ChooseCharacterRequest":
                break;
        }
    }
}
