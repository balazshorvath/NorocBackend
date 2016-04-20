package hu.noroc.gameworld.config;

import hu.noroc.common.data.model.NPCModel;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.repository.NPCRepo;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.gameworld.components.scripting.ScriptedNPC;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The configuration model of a World.
 *
 * Also can be used to get the object out of two JSONs.
 *
 * mainConf - general data
 * npcConf - NPCScriptConfigs
 *
 * Created by Oryk on 3/20/2016.
 */
public class WorldConfig {
    private final static Logger LOGGER = Logger.getLogger(WorldConfig.class.getName());
    private double mapWidth, mapHeight, areaSize;
    private List<ScriptedNPC> scripts = new ArrayList<>();
    private List<Spell> spells;

    private WorldConfig() {
    }

    public static WorldConfig getWorldConfig(String mainConf, String npcConf) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WorldConfig config = mapper.readValue(new File(mainConf), WorldConfig.class);
        NPCRepo repo = NorocDB.getInstance().getNpcRepo();

        List<NPCScriptConfig> configs = mapper.readValue(new File(npcConf), new TypeReference<List<NPCScriptConfig>>(){});
        configs.forEach(scriptConfig -> {
            NPCModel model;
            try {
                model = repo.findById(scriptConfig.getNpcId());
            } catch (IOException e) {
                LOGGER.warning("Could not initialize NPC " + e.getMessage());
                return;
            }
            if(model != null)
                config.scripts.add(new ScriptedNPC(model, scriptConfig));
            else
                LOGGER.warning("NPC model was not found in the database! Id: " + scriptConfig.getNpcId());
        });

        config.spells = NorocDB.getInstance().getSpellRepo().findAll();

        return config;
    }

    public void setMapWidth(double mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(double mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setAreaSize(double areaSize) {
        this.areaSize = areaSize;
    }

    public List<ScriptedNPC> getScripts(){
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

    public List<Spell> getSpells() {
        return spells;
    }
}
