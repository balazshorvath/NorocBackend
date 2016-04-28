package hu.noroc.gameworld.components.scripting;

import hu.noroc.common.data.model.NPCModel;
import hu.noroc.gameworld.components.behaviour.NPC;
import hu.noroc.gameworld.config.NPCScriptConfig;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Oryk on 3/20/2016.
 */
public class ScriptedNPC extends Scripted<NPC> {
    NPCModel model;
    NPCScriptConfig config;

    public ScriptedNPC(NPCModel model, NPCScriptConfig config) {
        super(model.getScript());
        this.model = model;
        this.config = config;
    }

    @Override
    protected NPC run() throws IOException, ScriptException, NoSuchAlgorithmException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        // evaluate script
        engine.eval(new FileReader(file));

        // get script object on which we want to implement the interface with
        Object obj = engine.get("npc");
        Invocable inv = (Invocable) engine;

        entity = inv.getInterface(obj, NPC.class);

        entity.setStats(model.getStats());
        entity.setName(model.getName());
        entity.setX(config.getX());
        entity.setY(config.getY());

        thread = new Thread(entity);
        thread.start();
        return this.entity;
    }
}
