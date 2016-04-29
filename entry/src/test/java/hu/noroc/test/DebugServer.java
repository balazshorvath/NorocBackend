package hu.noroc.test;

import hu.noroc.common.data.model.user.User;
import hu.noroc.test.utils.DBUtils;
import hu.noroc.test.utils.TestServer;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Oryk on 4/22/2016.
 */
public class DebugServer {
    TestServer server;
    private User user;

    @Test
    public void runServer() throws InterruptedException, IOException, NoSuchAlgorithmException {

        DBUtils.cleanDBs();
        user = DBUtils.createUser();
        DBUtils.initClasses();
        DBUtils.createCharacter("WARRIOR", user.getId());

        server = new TestServer();
        server.startGame();

        while(true){
            Thread.sleep(10);
        }
    }
    @After
    public void stop() throws InterruptedException {
        server.stopServer();
    }
}
