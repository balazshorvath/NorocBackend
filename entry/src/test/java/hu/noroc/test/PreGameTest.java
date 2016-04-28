package hu.noroc.test;

import hu.noroc.common.communication.response.ListCharacterResponse;
import hu.noroc.common.communication.response.ListWorldsResponse;
import hu.noroc.common.communication.response.LoginResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.user.User;
import hu.noroc.test.utils.DBUtils;
import hu.noroc.test.utils.TestClient;
import hu.noroc.test.utils.TestServer;
import hu.noroc.test.utils.common.TimeMeasure;
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
    private TimeMeasure measure;
    @Before
    public void setUp() throws Exception {
        DBUtils.cleanDBs();
        user = DBUtils.createUser();
        DBUtils.initClasses();
        DBUtils.createCharacter("WARRIOR", user.getId());

        user.setPassword("password");

        server = new TestServer();
        server.startGame();

        client = new TestClient();
        measure = new TimeMeasure();
    }

    @Test
    public void testPreGame() throws Exception {
        testLogin();
        testListings();
    }
    public void testLogin() throws Exception {
        measure.start("connection initialization");
        SimpleResponse response = client.init("localhost", 1234);
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);

        measure.start("failed password and username login");
        response = client.login("wronguser", "wrongpassword");
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        measure.start("failed password login");
        response = client.login(user.getUsername(), "wrongpassword");
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        measure.start("failed username login");
        response = client.login("wronguser", user.getPassword());
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.LOGIN_FAILED);

        measure.start("successful login");
        response = client.login(user.getUsername(), user.getPassword());
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);

        client.setSession(((LoginResponse) response).getSession());
    }

    public void testListings() throws Exception {
        PlayerCharacter character;
        ListWorldsResponse.WorldData world;

        measure.start("character listing");
        SimpleResponse response = client.listCharacters();
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);
        assertEquals(((ListCharacterResponse) response).getCharacters().size(), 1);

        character = ((ListCharacterResponse)response).getCharacters().get(0);

        measure.start("world listing");
        response = client.listWorlds();
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);
        assertEquals(((ListWorldsResponse)response).getWorlds().size(), 1);

        world = ((ListWorldsResponse)response).getWorlds().get(0);

        measure.start("character and world choosing");
        response = client.chooseCharacter(character.getId(),world.getId());
        measure.stopAndLog();
        assertEquals(response.getCode(), SimpleResponse.SUCCESS);
    }

    @After
    public void tearDown() throws Exception {
        server.stopServer();
    }
}
