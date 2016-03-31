package hu.noroc.gameworld.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 3/20/2016.
 */
public class WorldConfig {
    private double mapWidth, mapHeight, areaSize;
    private List<NPCScript> scripts = new ArrayList<>();

    private WorldConfig() {
    }

    public static WorldConfig getWorldConfig(String mainConf, String npcConf) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WorldConfig config = mapper.readValue(new File(mainConf), WorldConfig.class);

        List<ScriptConfig> configs = mapper.readValue(new File(npcConf), new TypeReference<List<ScriptConfig>>(){});
        configs.forEach(scriptConfig -> config.scripts.add(new NPCScript(new File(scriptConfig.getFile()), scriptConfig.getParams())));

        return config;
    }

    public List<NPCScript> getScripts(){
        return scripts;
    }


    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

    public double getAreaSize() {
        return areaSize;
    }
}
