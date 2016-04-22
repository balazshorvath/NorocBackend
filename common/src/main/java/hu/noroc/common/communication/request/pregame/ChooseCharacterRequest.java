package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Request for logging in with a character to a specified world server.
 *
 * Created by Oryk on 3/28/2016.
 */
public class ChooseCharacterRequest extends Request {
    private String worldId;
    private String characterId;

    public ChooseCharacterRequest() {
        this.type = ChooseCharacterRequest.class.getSimpleName();
    }
    public ChooseCharacterRequest(String worldId, String characterId) {
        this.type = ChooseCharacterRequest.class.getSimpleName();
        this.characterId = characterId;
        this.worldId = worldId;
    }

    public String getWorldId() {
        return worldId;
    }

    public void setWorldId(String worldId) {
        this.worldId = worldId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }
}
