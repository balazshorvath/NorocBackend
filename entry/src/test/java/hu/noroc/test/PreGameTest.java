package hu.noroc.test;

import hu.noroc.common.communication.response.LoginResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.user.User;
import hu.noroc.test.utils.DBUtils;
import hu.noroc.test.utils.TestClient;
import hu.noroc.test.utils.TestServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oryk on 4/19/2016.
 */
public class PreGameTest {
    private TestClient client;
    private TestServer server;
    private User user;
    @Before
    public void setUp() throws Exception {
        user = DBUtils.createUser();
        DBUtils.initClasses();
        user.setPassword("password");

        server = new TestServer();
        server.startGame();

        client = new TestClient();
    }

    @Test
    public void testLogin() throws Exception {
        SimpleResponse response = client.init("localhost", 50231);
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);

        response = client.login("wronguser", "wrongpassword");
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        response = client.login(user.getUsername(), "wrongpassword");
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        response = client.login("wronguser", user.getPassword());
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        response = client.login(user.getUsername(), user.getPassword());
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);

        client.setSession(((LoginResponse)response).getSession());
    }

    @Test
    public void testListings() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        server.stopServer();
    }
}
