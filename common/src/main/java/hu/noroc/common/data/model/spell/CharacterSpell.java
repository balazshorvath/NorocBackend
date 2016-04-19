package hu.noroc.common.data.model.spell;

/**
 * Created by Oryk on 4/14/2016.
 */
public class CharacterSpell extends Spell {
    /* in-game properties */
    private long lastCasted;
    /* upgrading */
    private String characterId;
    private int upgradesSpent;
    private SpellEffect[] effects = new SpellEffect[2];

    public long getLastCasted() {
        return lastCasted;
    }

    public void setLastCasted(long lastCasted) {
        this.lastCasted = lastCasted;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public int getUpgradesSpent() {
        return upgradesSpent;
    }

    public void setUpgradesSpent(int upgradesSpent) {
        this.upgradesSpent = upgradesSpent;
    }

    public SpellEffect[] getEffects() {
        return effects;
    }

    public void setEffects(SpellEffect[] effects) {
        this.effects = effects;
    }
}
