package hu.noroc.entry;

import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.entry.network.Client;
import hu.noroc.gameworld.components.World;
import hu.noroc.gameworld.config.WorldConfig;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Oryk on 3/28/2016.
 */
public class NorocEntry {
    private final static Logger LOGGER = Logger.getLogger(NorocEntry.class.getName());

    private static NorocDB database;
    private static Map<String, Client> clients = new HashMap<>();
    private static Map<String, World> worlds = new HashMap<>();

    public static void main(String[] args){
        if(args.length < 3) {
            System.out.printf("Params should be:\n" +
                    "{dburl} {dbname} [{mainConf1 npcConf1} {mainConf2 npcConf2} ...]\n");
            System.exit(-1);
        }
        database = NorocDB.getInstance(args[0], args[1]);
        for(int i = 2; i < args.length; i += 2) {
            WorldConfig config = null;
            World world = new World();
            String id;
            try {
                config = WorldConfig.getWorldConfig(args[i], args[i + 1]);
            } catch (IOException e) {
                LOGGER.warning("Couldn't load WorldConfig:\n" + e.getMessage());
                continue;
            }
            world.initWorld(config);
            id = new ObjectId().toString();
            worlds.put(id, world);
            LOGGER.info("World initialized: " + id);
        }
    }

}
