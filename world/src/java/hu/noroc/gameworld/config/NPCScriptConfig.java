package hu.noroc.gameworld.config;


/**
 * This configuration class contains specific data for npcs to spawn.
 *
 * If there's ever any information for an npc, which cannot be "static" (stored in DB NPC),
 * it should be here.
 *
 * This data is parsed from the "npcConfig.json" which is part of a WorldConfig.
 *
 * Created by Oryk on 3/20/2016.
 */
public class NPCScriptConfig {
    private String npcId;
    private double x, y;

    public String getNpcId() {
        return npcId;
    }

    public void setNpcId(String npcId) {
        this.npcId = npcId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
