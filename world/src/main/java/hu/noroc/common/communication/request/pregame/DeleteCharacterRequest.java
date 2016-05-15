package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 2016. 05. 15..
 */
public class DeleteCharacterRequest extends Request {
    private String characterId;

    public DeleteCharacterRequest() {
        super.type = "DeleteCharacterRequest";
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }
}
