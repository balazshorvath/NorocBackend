package hu.noroc.entry;

import hu.noroc.common.communication.request.PingRequest;
import hu.noroc.common.communication.request.ReconnectRequest;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.pregame.CreateCharacterRequest;
import hu.noroc.common.communication.request.pregame.DeleteCharacterRequest;
import hu.noroc.common.communication.response.ListCharacterResponse;
import hu.noroc.common.communication.response.standard.ErrorResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.communication.response.standard.SuccessResponse;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.user.User;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.entry.config.EntryConfig;
import hu.noroc.entry.network.Client;
import hu.noroc.entry.network.GamingClient;
import hu.noroc.entry.security.Compressor;
import hu.noroc.entry.security.SecurityUtils;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.config.WorldConfig;
import hu.noroc.gameworld.messaging.sync.SyncMessage;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Oryk on 3/28/2016.
 */
//TODO remove sessions and also remove player from player ticker
public class NorocEntry {
    private final static Logger LOGGER = Logger.getLogger(NorocEntry.class.getName());

    private static NorocDB database;
    public static final Map<String, Client> clients = new HashMap<>();
    public static final Map<String, World> worlds = new HashMap<>();

    private static Thread tcpServer;
    private static Map<String, Thread> worldListeners = new HashMap<>();

    private static boolean running = true;
    public static DatagramSocket socket;


    public static void main(String[] args) throws InterruptedException {
        if(args.length < 3 && (args.length % 2) != 1) {
            System.out.printf("Params should be:\n" +
                    "{entryConfig} [{mainConf1 npcConf1} {mainConf2 npcConf2} ...]\n");
            System.exit(-1);
        }
        try {
            EntryConfig.loadConfig(args[0]);
        } catch (IOException e) {
            LOGGER.warning("Could not load config file. Closing.");
        }

        database = NorocDB.getInstance(
                EntryConfig.getValue("dbUrl"),
                EntryConfig.getValue("dbName")
        );
        if(EntryConfig.getValue("isLive") != null) {
            try {
                System.setErr(new PrintStream(new FileOutputStream("/var/log/NorocError.log")));

                LOGGER.addHandler(new FileHandler(EntryConfig.getValue("entryLog")));
                LOGGER.setUseParentHandlers(false);
                World.LOGGER.addHandler(new FileHandler(EntryConfig.getValue("worldLog")));
                World.LOGGER.setUseParentHandlers(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i = 1; i < args.length; i += 2) {
            WorldConfig config = null;
            World world = null;
            String id;
            try {
                config = WorldConfig.getWorldConfig(args[i], args[i + 1]);
            } catch (IOException e) {
                LOGGER.warning("Couldn't load WorldConfig:\n" + e.getMessage());
                continue;
            }
            world = World.initWorld(config);
            id = SecurityUtils.randomString(32);
            worlds.put(id, world);
            worldListeners.put(id, getWorldListener(world));
            LOGGER.info("World initialized. Id: " + id + " with name: " + world.getName());
        }
        getTcpServer().start();
        getUDPServer().start();
        worldListeners.values().stream().forEach(Thread::start);

        while(running){
            Thread.sleep(1000);
        }
    }

    private static Thread getUDPServer(){
        return new Thread(() -> {
            String portS = EntryConfig.getValue("port");
            int port = 0;
            if(portS != null)
                port = Integer.parseInt(portS);
            if(port == 0)
                port = 1234;

            ObjectMapper mapper = new ObjectMapper();
            PingRequest request;
            Client client;
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                e.printStackTrace();
                return;
            }
            DatagramPacket packet;
            byte[] received = new byte[4096];
            while(running){
                packet = new DatagramPacket(received, received.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    int len = 0;
                    for (int i = 0; i < packet.getData().length; i++) {
                        if(packet.getData()[i] == '\n'){
                            len = i - 1;
                            break;
                        }
                        if(packet.getData()[i] == 0){
                            break;
                        }
                    }
                    String msg = new String(packet.getData(), 0, len);
                    request = mapper.readValue(Compressor.gunzip(msg), PingRequest.class);


                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                client = clients.get(request.getSession());

                if(client != null){
                    client.udpPing(request, packet.getAddress(), packet.getPort());
                }
            }
        });
    }

    private static Thread getWorldListener(World world){
        return new Thread(() -> {
            SyncMessage msg;
            Client client = null;
            while(running){
                try {
                    msg = world.getSyncMessage();
                    client = clients.get(msg.getSession());
//                    if(!client.getState().equals(Client.ClientState.CONNECTED)
//                            || !client.getState().equals(Client.ClientState.CONNECTING))
//                        world.logoutCharacter(client.getUser().getId(), client.getCharacterId());

                    client.sendSync(msg.getEvent().createMessage());
                } catch(Exception ignored) {
                    if(!(ignored instanceof NullPointerException)
                        && client != null && !client.getState().equals(Client.ClientState.CONNECTING)){
                        //TODO: do it only, when IOException
                        LOGGER.info("Client disconnected");
                        ignored.printStackTrace();
                        world.logoutCharacter(client.getUser().getId(), client.getCharacterId());
                    }
                }
            }
            LOGGER.info("World listener(" + world.getName() + ") is down.");
        });
    }

    private static Thread getTcpServer(){
        if(tcpServer != null && !tcpServer.isAlive())
            return tcpServer;
        tcpServer = new Thread(() -> {
            String portS = EntryConfig.getValue("port");
            int port = 0;
            if(portS != null)
                port = Integer.parseInt(portS);
            if(port == 0)
                port = 1234;

            ServerSocket server;
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
                LOGGER.warning("Stopping...");
                return;
            }
            Socket socket;

            while(running){
                try {
                    socket = server.accept();
                    prepareClient(socket).start();
                } catch (IOException e) {
                    LOGGER.severe("TCP server is closed." + e.getMessage());
                }
            }
            try {
                server.close();
                LOGGER.info("TCP server is closed.");
            } catch (IOException ignore) {
            }
        });
        return tcpServer;
    }

    public static void stopServer() throws InterruptedException {
        LOGGER.info("Shutting down main threads.");
        running = false;
        Thread.sleep(1000);
        if(!tcpServer.getState().equals(Thread.State.TERMINATED))
            tcpServer.interrupt();

        worldListeners.values().stream()
                .filter(thread -> !thread.getState().equals(Thread.State.TERMINATED))
                .forEach(Thread::interrupt);

        clients.values().forEach(Client::disconnect);
        Thread.sleep(1000);

        clients.values().stream().filter(client -> !client.getSocket().isClosed()).forEach(Client::forceDisconnect);
        Thread.sleep(1000);
    }
    public static Thread prepareClient(Socket socket){
        return new Thread(() -> {
            try {
                socket.setSoTimeout(5000);
            } catch (SocketException e) {
                LOGGER.info("Connection problem.");
                return;
            }
            String message = null;
            try {
                message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            } catch (SocketTimeoutException ignore) {
            } catch (IOException e) {
                LOGGER.info("Connection problem.");
                return;
            }
            if(message != null){
                try {
                    Request request = new ObjectMapper().readValue(Compressor.gunzip(message), Request.class);
                    if(request instanceof ReconnectRequest){
                        GamingClient client = (GamingClient) clients.get(request.getSession());
                        if(client == null){
                            socket.getOutputStream().write(
                                    Compressor.gzip(
                                            (new ObjectMapper().writeValueAsString(new ErrorResponse(SimpleResponse.INTERNAL_ERROR)))
                                            + '\n').getBytes()
                            );
                            socket.getOutputStream().flush();
                            socket.close();
                            return;
                        }
                        if(client.getState() == Client.ClientState.CONNECTED){
                            LOGGER.warning("Got reconnect request for client, but client was online.");
                            return;
                        }
                        socket.setSoTimeout(60000);
                        if(client.isOnline())
                            client.disconnect();
                        client.setSocket(socket);
                        client.reconnect();
                        new Thread(client).start();
                        LOGGER.info("Client reconnected.");
                        return;
                    }else {
                        return;
                    }
                } catch (IOException e) {
                    LOGGER.info("Connection problem.");
                    return;
                }
            }
            try {
                socket.setSoTimeout(60000);
            } catch (SocketException e) {
                LOGGER.info("Connection problem.");
                return;
            }
            String session;
            while(clients.containsKey(session = SecurityUtils.randomString(32)));

            GamingClient client = new GamingClient(socket, session, null);
            new Thread(client).start();
            clients.put(client.getSession(), client);
            LOGGER.info("Client connected.");
        });
    }

    public static SimpleResponse createCharacter(CreateCharacterRequest request, User user) throws IOException {
        if(database.getCharacterRepo().findByUser(user.getId()).size() >= 4)
            return new ErrorResponse(SimpleResponse.INTERNAL_ERROR);

        if(request.getName().length() <= 3)
            return new ErrorResponse(SimpleResponse.NAME_TAKEN);

        if(database.getCharacterRepo().findBy("name", request.getName()).size() != 0)
            return new ErrorResponse(SimpleResponse.NAME_TAKEN);

        CharacterClass characterClass = database.getCharacterClassRepo().findByCode(request.getClassId());
        if(characterClass == null)
            return new ErrorResponse(SimpleResponse.INVALID_REQUEST);

        PlayerCharacter character = new PlayerCharacter(request.getName(), user.getId(), request.getClassId());

        List<Spell> spells = new ArrayList<>();
        for (String s : characterClass.getSpells()) {
            Spell spell = database.getSpellRepo().findById(s);
            if(spell != null)
                spells.add(spell);
        }

        character.setX(256.0);
        character.setY(170.0);
        character.setSpells(
                spells.stream().collect(
                        Collectors.toMap(
                                Spell::getId,
                                CharacterSpell::new
                        )
                )
        );
        character.setXp(0);

        final String id = database.getCharacterRepo().insert(character);
        //TODO: should it be session?
        character = database.getCharacterRepo().findById(id);
        character.getSpells().forEach((s, characterSpell) -> characterSpell.setOwnerId(id));
        character.setId(id);
        database.getCharacterRepo().save(character);

        return new ListCharacterResponse(
                database.getCharacterRepo().findByUser(user.getId())
        );
    }

    public static SimpleResponse deleteCharacter(DeleteCharacterRequest request, User user) throws IOException {
        PlayerCharacter character = database.getCharacterRepo().findById(request.getCharacterId());

        if(character != null && !character.getUserId().equals(user.getId()))
            return new ErrorResponse(SimpleResponse.INTERNAL_ERROR);

        database.getCharacterRepo().delete(request.getCharacterId());

        return new SuccessResponse();
    }
}
