package hu.noroc.gameworld.components.behaviour.spell;

import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.components.behaviour.Being;

/**
 * Created by Oryk on 4/27/2016.
 */
public abstract class SpellLogic extends SpellEffect {
    private String spellName;
    private String spellId;
    private String characterId;

    public SpellLogic(SpellEffect spellEffect, String spellName, String spellId, String characterId) {
        super(spellEffect);
        this.spellName = spellName;
        this.spellId = spellId;
        this.characterId = characterId;
    }

    public SpellLogic(SpellLogic logic) {
        super(logic);
        this.spellName = logic.spellName;
        this.spellId = logic.spellId;
        this.characterId = logic.characterId;
    }

    //TODO: separate method for "friendly" and "unfriendly" targets
    public abstract void effect(Being being);

    public String getSpellName() {
        return spellName;
    }

    public String getSpellId() {
        return spellId;
    }

    public String getCharacterId() {
        return characterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpellLogic that = (SpellLogic) o;

        if (spellName != null ? !spellName.equals(that.spellName) : that.spellName != null) return false;
        if (!spellId.equals(that.spellId)) return false;
        return characterId.equals(that.characterId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (spellName != null ? spellName.hashCode() : 0);
        result = 31 * result + spellId.hashCode();
        result = 31 * result + characterId.hashCode();
        return result;
    }
}
