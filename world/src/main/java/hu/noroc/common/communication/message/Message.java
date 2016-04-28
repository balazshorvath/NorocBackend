package hu.noroc.common.communication.message;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * A Message is sent by the server entities to the clients.
 * They are basically synchronization messages.
 *
 * Created by Oryk on 3/28/2016.
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AttackMessage.class, name = "AttackMessage"),
    @JsonSubTypes.Type(value = DirectionalMessage.class, name = "DirectionalMessage"),
    @JsonSubTypes.Type(value = TargetedMessage.class, name = "TargetedMessage")
})
public class Message {
    protected String type;
    protected EntityType entityType;
    protected String entityId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}
