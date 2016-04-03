package hu.noroc.common.data.model.spell;


/**
 * Created by Oryk on 4/1/2016.
 */
public class Spell {
    private String id;
    private SpellCost cost;
    private SpellEffect effect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpellEffect getEffect() {
        return effect;
    }

    public void setEffect(SpellEffect effect) {
        this.effect = effect;
    }

    public SpellCost getCost() {
        return cost;
    }

    public void setCost(SpellCost cost) {
        this.cost = cost;
    }
}