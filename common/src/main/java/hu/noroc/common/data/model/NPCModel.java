package hu.noroc.common.data.model;

import hu.noroc.common.data.model.character.CharacterStat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oryk on 4/1/2016.
 */
public class NPCModel {
    private String id;
    private String name;
    private String script;
    private Map<String, String> scriptParams = new HashMap<>();
    private CharacterStat stats;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Map<String, String> getScriptParams() {
        return scriptParams;
    }

    public void setScriptParams(Map<String, String> scriptParams) {
        this.scriptParams = scriptParams;
    }

    public CharacterStat getStats() {
        return stats;
    }

    public void setStats(CharacterStat stats) {
        this.stats = stats;
    }
}
