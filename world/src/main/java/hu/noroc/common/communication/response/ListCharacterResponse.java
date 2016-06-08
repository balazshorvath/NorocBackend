package hu.noroc.common.communication.response;

import hu.noroc.common.communication.message.models.PlayerCharacterResponse;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.communication.response.standard.SimpleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oryk on 4/14/2016.
 */
public class ListCharacterResponse extends SimpleResponse {
    private List<PlayerCharacterResponse> characters;

    public ListCharacterResponse() {
        super(SUCCESS);
    }

    public ListCharacterResponse(List<PlayerCharacter> characters) {
        super(SUCCESS);
        this.characters = new ArrayList<>(characters.size());
        characters.forEach(character -> this.characters.add(new PlayerCharacterResponse(character, 0, 0, new CharacterStat())));
    }

    public List<PlayerCharacterResponse> getCharacters() {
        return characters;
    }

    public void setCharacters(List<PlayerCharacterResponse> characters) {
        this.characters = characters;
    }
}
