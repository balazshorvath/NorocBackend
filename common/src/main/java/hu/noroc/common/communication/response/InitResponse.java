package hu.noroc.common.communication.response;

import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.npc.NPCData;

import java.util.List;

/**
 * Created by Oryk on 4/17/2016.
 */
public class InitResponse extends SimpleResponse {
    private PlayerCharacter self;
    private List<NPCData> npcs;

    public InitResponse() {
        super(SimpleResponse.SUCCESS);
        super.type = "InitResponse";
    }

    public PlayerCharacter getSelf() {
        return self;
    }

    public void setSelf(PlayerCharacter self) {
        this.self = self;
    }

    public List<NPCData> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<NPCData> npcs) {
        this.npcs = npcs;
    }
}
