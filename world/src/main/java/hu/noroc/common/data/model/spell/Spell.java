package hu.noroc.common.data.model.spell;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.noroc.gameworld.components.behaviour.spell.SpellLogic;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/1/2016.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = CharacterSpell.class, name = "CharacterSpell")
})
public class Spell {
    protected String id;
    protected int ordinal;
    protected String name;
    protected String description;
    protected double radius, alpha;
    protected int cooldown;
    protected int castTime;
    protected SpellCost cost;
    protected SpellEffect effect;
    protected List<SpellEffect.SpellType> acceptedUpgrades = new ArrayList<>();
    protected int maxUpgrades;

    public Spell() {
    }

    public Spell(Spell spell) {
        this.id = spell.id;
        this.name = spell.name;
        this.description = spell.description;
        this.radius = spell.radius;
        this.alpha = spell.alpha;
        this.cooldown = spell.cooldown;
        this.castTime = spell.castTime;
        this.cost = spell.cost;
        this.effect = spell.effect;
        this.acceptedUpgrades = spell.acceptedUpgrades;
        this.maxUpgrades = spell.maxUpgrades;
        this.ordinal = spell.ordinal;
    }

    @ObjectId
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @ObjectId
    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
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

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCastTime() {
        return castTime;
    }

    public void setCastTime(int castTime) {
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

    public void setMaxUpgrades(int maxUpgrades) {
        this.maxUpgrades = maxUpgrades;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
