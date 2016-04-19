package hu.noroc.common.data.model.spell;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/1/2016.
 */
public class Spell {
    protected String id;
    protected double radius, alpha;
    protected long cooldown;
    protected long castTime;
    protected SpellCost cost;
    protected SpellEffect effect;
    protected List<SpellEffect.SpellType> acceptedUpgrades = new ArrayList<>();
    protected int maxUpgrades;

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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getCastTime() {
        return castTime;
    }

    public void setCastTime(long castTime) {
        this.castTime = castTime;
    }

    public List<SpellEffect.SpellType> getAcceptedUpgrades() {
        return acceptedUpgrades;
    }

    public void setAcceptedUpgrades(List<SpellEffect.SpellType> acceptedUpgrades) {
        this.acceptedUpgrades = acceptedUpgrades;
    }

    public int getMaxUpgrades() {
        return maxUpgrades;
    }
}
