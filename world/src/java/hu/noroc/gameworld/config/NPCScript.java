package hu.noroc.gameworld.config;

import hu.noroc.gameworld.components.NPC;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Oryk on 3/20/2016.
 */
public class NPCScript {
    //TODO: SET HANDLER
    private final static Logger LOGGER = Logger.getLogger(NPCScript.class.getName());

    File file;
    String fileHash;
    Map<String, String> params;
    NPC npc;
    Thread npcThread;

    public NPCScript(File file, Map<String, String> params) {
        this.file = file;
        this.params = params;
    }

    public NPC runNPC() {
        try {
            if(shouldReRun())
                return run();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warning("Failed to create hash for js file : " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warning("Could not find js file for NPC : " + e.getMessage());
        } catch (ScriptException e) {
            LOGGER.warning("Error in js : " + e.getMessage());
        }
        return npc;
    }

    public void forceStop(){
        if(this.npcThread.isAlive())
            this.npcThread.interrupt();
    }

    private boolean shouldReRun() throws NoSuchAlgorithmException, IOException {
        if(npc == null)
            return true;

        MessageDigest md = MessageDigest.getInstance("MD5");
        String newHash = new String(md.digest(Files.readAllBytes(file.toPath())));

        return !newHash.equals(fileHash);

    }

    private NPC run() throws IOException, ScriptException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        fileHash = new String(md.digest(Files.readAllBytes(file.toPath())));
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        // evaluate script
        engine.eval(new FileReader(file));

        params.forEach(engine::put);

        // get script object on which we want to implement the interface with
        Object obj = engine.get("npc");
        Invocable inv = (Invocable) engine;

        npc = inv.getInterface(obj, NPC.class);
        npcThread = new Thread(npc);
        npcThread.start();
        return this.npc;
    }

    public NPC getNpc() {
        return npc;
    }
}
