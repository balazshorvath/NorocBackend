package hu.noroc.gameworld.components.scripting;

import hu.noroc.common.data.model.NPCModel;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.gameworld.Area;
import hu.noroc.gameworld.components.behaviour.Being;
import hu.noroc.gameworld.components.behaviour.ActingEntity;
import hu.noroc.gameworld.components.behaviour.NPC;
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import hu.noroc.gameworld.config.NPCScriptConfig;
import hu.noroc.gameworld.messaging.Event;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * Created by Oryk on 3/20/2016.
 */
public class ScriptedNPC extends Scripted<NPC> implements Being, ActingEntity {
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

    @Override
    public void tick() {

    }

    @Override
    public boolean isInside(double x, double y) {
        return false;
    }

    @Override
    public void newEvent(Event message) {

    }

    @Override
    public void attacked(SpellLogic logic, Being caster) {

    }

    @Override
    public Set<BuffLogic> getEffects() {
        return null;
    }

    @Override
    public CharacterStat getStats() {
        return null;
    }

    @Override
    public void setStats(CharacterStat stats) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public void setX(double x) {

    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public void setY(double y) {

    }

    @Override
    public Integer getArea() {
        return null;
    }

    @Override
    public void setArea(Area area) {

    }

}
