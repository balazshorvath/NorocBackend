package hu.noroc.common.communication.request;

import hu.noroc.common.communication.request.ingame.*;
import hu.noroc.common.communication.request.pregame.*;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * A Request is sent by the client to the entry/world/area/player object to handle.
 *
 * Created by Oryk on 3/28/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        /*General*/
        @JsonSubTypes.Type(value = ReconnectRequest.class, name = "ReconnectRequest"),
        @JsonSubTypes.Type(value = PauseRequest.class, name = "PauseRequest"),
        /*Pre-game*/
        @JsonSubTypes.Type(value = LoginRequest.class, name = "LoginRequest"),
        @JsonSubTypes.Type(value = ListWorldsRequest.class, name = "ListWorldsRequest"),
        @JsonSubTypes.Type(value = ListCharactersRequest.class, name = "ListCharactersRequest"),
        @JsonSubTypes.Type(value = ChooseCharacterRequest.class, name = "ChooseCharacterRequest"),
        /*Character c/d*/
        @JsonSubTypes.Type(value = CreateCharacterRequest.class, name = "CreateCharacterRequest"),
        @JsonSubTypes.Type(value = DeleteCharacterRequest.class, name = "DeleteCharacterRequest"),
        /*In-game*/
        @JsonSubTypes.Type(value = InitRequest.class, name = "InitRequest"),
        @JsonSubTypes.Type(value = PlayerAttackRequest.class, name = "PlayerAttackRequest"),
        @JsonSubTypes.Type(value = PlayerInteractRequest.class, name = "PlayerInteractRequest"),
        @JsonSubTypes.Type(value = PlayerMoveRequest.class, name = "PlayerMoveRequest"),
        @JsonSubTypes.Type(value = RespawnRequest.class, name = "RespawnRequest"),
        @JsonSubTypes.Type(value = PlayerEquipRequest.class, name = "PlayerEquipRequest")
})
public class Request {
    protected String type;
    protected String session;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
