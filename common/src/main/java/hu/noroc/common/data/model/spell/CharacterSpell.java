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

}
