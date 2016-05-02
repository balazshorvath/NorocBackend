package hu.noroc.entry;

import com.mongodb.gridfs.CLI;
import hu.noroc.common.communication.request.ReconnectRequest;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.response.standard.ErrorResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.entry.config.EntryConfig;
import hu.noroc.entry.network.Client;
import hu.noroc.entry.network.GamingClient;
import hu.noroc.entry.security.SecurityUtils;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.config.WorldConfig;
import hu.noroc.gameworld.messaging.sync.SyncMessage;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Oryk on 3/28/2016.
 */
public class NorocEntry {
    private final static Logger LOGGER = Logger.getLogger(NorocEntry.class.getName());

    private static NorocDB database;
    public static final Map<String, Client> clients = new HashMap<>();
    public static final Map<String, World> worlds = new HashMap<>();

    private static Thread tcpServer;
    private static Map<String, Thread> worldListeners = new HashMap<>();

    private static boolean running = true;


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
        worldListeners.values().stream().forEach(Thread::start);

        while(running){
            Thread.sleep(1000);
        }
    }

    private static Thread getWorldListener(World world){
        return new Thread(() -> {
            SyncMessage msg;
            ObjectMapper mapper = new ObjectMapper();
            Client client;
            OutputStream stream;
            while(running){
                try {
                    msg = world.getSyncMessage();
                    client = clients.get(msg.getSession());
                    stream = client.getSocket().getOutputStream();
                    stream.write((
                            mapper.writeValueAsString(
                                    msg.getEvent().createMessage()
                            ) + '\n'
                    ).getBytes());
                    stream.flush();
                } catch(Exception ignored) {
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
                    Request request = new ObjectMapper().readValue(message, Request.class);
                    if(request instanceof ReconnectRequest){
                        GamingClient client = (GamingClient) clients.get(request.getSession());
                        if(client == null){
                            socket.getOutputStream().write(
                                    new ObjectMapper().writeValueAsBytes(new ErrorResponse(SimpleResponse.INTERNAL_ERROR))
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
                        new Thread(client).start();
                        LOGGER.info("Client reconnected.");
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
            GamingClient client = new GamingClient(socket, SecurityUtils.randomString(32), null);
            new Thread(client).start();
            clients.put(client.getSession(), client);
            LOGGER.info("Client connected.");
        });
    }
}
