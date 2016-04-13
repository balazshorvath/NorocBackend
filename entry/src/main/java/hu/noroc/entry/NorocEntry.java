package hu.noroc.entry;

import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.entry.config.EntryConfig;
import hu.noroc.entry.network.Client;
import hu.noroc.entry.security.SecurityUtils;
import hu.noroc.gameworld.World;
import hu.noroc.gameworld.config.WorldConfig;
import hu.noroc.gameworld.messaging.sync.SyncMessage;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
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
    private static final Map<String, World> worlds = new HashMap<>();

    private static Thread tcpServer;
    private static Map<String, Thread> worldListeners = new HashMap<>();


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

        database = NorocDB.getInstance(args[0], args[1]);
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
            id = new ObjectId().toString();
            worlds.put(id, world);
            worldListeners.put(id, getWorldListener(world));
            LOGGER.info("World initialized: " + id);
        }
        getTcpServer().start();
        worldListeners.values().parallelStream().forEach(Thread::start);

        while(true){
            //TODO: here accept console request
            Thread.sleep(1000);
        }
    }

    private static Thread getWorldListener(World world){
        return new Thread(() -> {
            SyncMessage msg;
            ObjectMapper mapper = new ObjectMapper();
            Client client;
            OutputStream stream;
            while(true){
                try{
                    msg = world.getSyncMessage();
                    client = clients.get(msg.getSession());
                    stream = client.getSocket().getOutputStream();
                    stream.write(NetworkData.prepareRSAData(
                            mapper.writeValueAsString(
                                    NetworkData.getFromEvent(msg.getMessage())
                            ),
                            client.getKey().getPublic()
                    ));
                    stream.flush();
                }catch(NullPointerException | IOException e){
                    LOGGER.info("getWorldListener exception.");
                }
            }
        });
    }

    private static Thread getTcpServer(){
        if(tcpServer != null)
            return tcpServer;
        tcpServer = new Thread(() -> {
            //TODO: accept clients, create Client, start Client
            String portS = EntryConfig.getValue("port");
            int port = 63001;
            if(portS != null)
                port = Integer.getInteger(portS);

            ServerSocket server;
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
                LOGGER.warning("Stopping...");
                return;
            }
            Socket socket;
            Client client;

            while(true){
                try {
                    socket = server.accept();
                    client = new Client(socket, new ObjectId().toString(), null);
                    clients.put(client.getSession(), client);
                } catch (IOException e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        });
        return tcpServer;
    }

}
