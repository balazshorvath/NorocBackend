package hu.noroc.gameworld.messaging;

import hu.noroc.common.communication.message.InitMessage;
import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.message.models.PlayerCharacterResponse;
import hu.noroc.common.communication.response.standard.SimpleResponse;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.components.behaviour.spell.BuffLogic;
import hu.noroc.gameworld.messaging.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oryk on 4/17/2016.
 */
public class InitResponse extends Event {
    private InGamePlayer self;


    public InGamePlayer getSelf() {
        return self;
    }

    public void setSelf(InGamePlayer self) {
        this.self = self;
    }


    @Override
    public Message createMessage() {
        InitMessage message = new InitMessage();
        message.setSelf(self);
        return message;
    }

    public static class InGamePlayer {
        private PlayerCharacterResponse character;
        private Set<BuffLogic> effects = new HashSet<>();

        public InGamePlayer(Player player) {
            this.character = new PlayerCharacterResponse(player.getCharacter());
            this.effects = player.getEffects();
        }

        public PlayerCharacterResponse getCharacter() {
            return character;
        }

        public void setCharacter(PlayerCharacterResponse character) {
            this.character = character;
        }

        public Set<BuffLogic> getEffects() {
            return effects;
        }

        public void setEffects(Set<BuffLogic> effects) {
            this.effects = effects;
        }
    }

}
