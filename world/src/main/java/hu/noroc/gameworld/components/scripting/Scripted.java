package hu.noroc.gameworld.components.scripting;


import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by Oryk on 4/1/2016.
 */
public abstract class Scripted<Entity> {
    //TODO: SET HANDLER
    private final static Logger LOGGER = Logger.getLogger(Scripted.class.getName());
    protected File file;
    protected String fileHash;
    protected Thread thread;
    protected Entity entity;

    public Scripted(String script){
        file = new File(script);
    }

    public Entity runEntity() {
        try {
            if(shouldReRun()) {
                run();
                MessageDigest md = MessageDigest.getInstance("MD5");
                fileHash = new String(md.digest(Files.readAllBytes(file.toPath())));
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warning("Failed to create hash for js file : " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warning("Could not find js file for " + this.getClass().getSimpleName() + " : " + e.getMessage());
        } catch (ScriptException e) {
            LOGGER.warning("Error in js : " + e.getMessage());
        }
        return entity;
    }

    public void forceStop(){
        if(this.thread.isAlive())
            this.thread.interrupt();
    }

    protected boolean shouldReRun() throws NoSuchAlgorithmException, IOException {
        if(entity == null)
            return true;

        MessageDigest md = MessageDigest.getInstance("MD5");
        String newHash = new String(md.digest(Files.readAllBytes(file.toPath())));

        return !newHash.equals(fileHash);
    }

    abstract protected Entity run() throws IOException, ScriptException, NoSuchAlgorithmException;

    public Entity getEntity() {
        return entity;
    }
}
