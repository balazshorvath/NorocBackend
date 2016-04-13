package hu.noroc.common.data.model.spell;

/**
 * Created by Oryk on 4/1/2016.
 */
public class SpellCost {
    private int amount;
    private CostType type;

    public enum CostType{
        MANA,
        HEALTH
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CostType getType() {
        return type;
    }

    public void setType(CostType type) {
        this.type = type;
    }
}
