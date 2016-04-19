package hu.noroc.common.communication.response;

import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.PlayerCharacter;

/**
 * Created by Oryk on 4/17/2016.
 */
public class InitResponse extends SimpleResponse {
    private PlayerCharacter self;
    private

    public InitResponse() {
        super(SimpleResponse.SUCCESS);
    }


}
