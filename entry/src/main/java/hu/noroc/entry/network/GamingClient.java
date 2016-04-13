package hu.noroc.entry.network;

import hu.noroc.common.data.model.user.User;
import hu.noroc.entry.security.SecurityUtils;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.IOException;
import java.io.InputStream;
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
    public boolean initRSA(){
        byte[] buffer = new byte[1024];
        try {
            SecurityUtils.generateKey(this);
            socket.getOutputStream().write(this.key.getPublic().getEncoded());
            int bytes = socket.getInputStream().read(buffer, 0, 1024);
            if(bytes != 1024)
                return false;

            this.clientPublic = new RSAPublicKeyImpl(buffer);
            this.online = true;
        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            return false;
        }

        return true;
    }

    @Override
    public void run() {
        InputStream stream;
        byte[] buffer = new byte[1024];

        while(online){

        }
    }
}
