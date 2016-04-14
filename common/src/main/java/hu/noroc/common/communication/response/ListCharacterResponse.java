package hu.noroc.common.communication.response;

import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;

import java.util.List;

/**
 * Created by Oryk on 4/14/2016.
 */
public class ListCharacterResponse extends SimpleResponse {
    private List<PlayerCharacter> characters;

    public ListCharacterResponse() {
        super(SUCCESS);
    }

    public ListCharacterResponse(List<PlayerCharacter> characters) {
        super(SUCCESS);
        this.characters = characters;
    }

    public List<PlayerCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(List<PlayerCharacter> characters) {
        this.characters = characters;
    }
}
