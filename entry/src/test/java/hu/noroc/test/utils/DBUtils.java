package hu.noroc.test.utils;

import hu.noroc.common.data.model.Item;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.CharacterSpell;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.data.model.spell.SpellCost;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.common.data.model.user.User;
import hu.noroc.common.mongodb.NorocDB;
import hu.noroc.test.utils.common.ArrayListBuilder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oryk on 4/19/2016.
 */
public class DBUtils {
    public static NorocDB db = NorocDB.getInstance("localhost", "Noroc");
    private static int userCounter = 0;
    private static int characterCounter = 0;
    private static int itemCounter = 0;

    public static User createUser() throws IOException, NoSuchAlgorithmException {
        User user = new User();

        user.setEmail("mail" + userCounter + "@gmail.com");
        user.setUsername("user" + userCounter);
        user.setPassword(
                new String(Base64.getEncoder().encode(
                        MessageDigest.getInstance("SHA-256").digest(("password" + userCounter).getBytes())
                ))
        );

        String id = db.getUserRepo().insert(user);
        userCounter++;
        return db.getUserRepo().findById(id);
    }

    public static PlayerCharacter createCharacter(String classCode, String userId) throws IOException {
        PlayerCharacter character = new PlayerCharacter(classCode + characterCounter, userId, classCode);
        CharacterClass characterClass = db.getCharacterClassRepo().findByCode(classCode);
        List<Spell> spells = new ArrayList<>();
        for (String s : characterClass.getSpells()) {
            Spell spell = db.getSpellRepo().findById(s);
            if(spell != null)
                spells.add(spell);
        }

        character.setX(256.0);
        character.setY(170.0);
        character.setSpells(
                spells.stream().collect(
                        Collectors.toMap(
                                Spell::getId,
                                CharacterSpell::new
                        )
                )
        );
        character.setXp(0);

        String id = db.getCharacterRepo().insert(character);
        character.getSpells().forEach((s, characterSpell) -> characterSpell.setOwnerId(id));
        character.setId(id);
        db.getCharacterRepo().save(character);
        characterCounter++;

        return db.getCharacterRepo().findById(id);
    }

    public static void initClasses() throws IOException {
        CharacterClass characterClass = new CharacterClass();
        CharacterStat stat = new CharacterStat();
        // Da default spell
        List<Spell> spell = initWarriorSpell();

        // Warrior stats
        stat.health = 500;
        stat.mana = 0;

        stat.armor = 10;
        stat.magicResist = 0;

        stat.intellect = 0;
        stat.spirit = 0;
        stat.stamina = 0;
        stat.strength = 0;

        stat.speed = 0.04;

        // Warrior class
        characterClass.setCode("WARRIOR");
        characterClass.setName("Warrior");

        characterClass.setSpells(
                new ArrayListBuilder<String>(4)
                        .add(spell.get(0).getId())
                        .add(spell.get(1).getId())
                        .add(spell.get(2).getId())
                        .add(spell.get(3).getId())
                        .get()
        );
        characterClass.setStat(stat);
        db.getCharacterClassRepo().insert(characterClass);

        characterClass = new CharacterClass();
        stat = new CharacterStat();
        // Da default spell
        spell = initRogueSpell();

        // Warrior stats
        stat.health = 400;
        stat.mana = 200;

        stat.armor = 5;
        stat.magicResist = 5;

        stat.intellect = 0;
        stat.spirit = 0;
        stat.stamina = 0;
        stat.strength = 0;

        stat.speed = 0.04;

        // Warrior class
        characterClass.setCode("ROGUE");
        characterClass.setName("Rogue");

        characterClass.setSpells(
                new ArrayListBuilder<String>(4)
                        .add(spell.get(0).getId())
                        .add(spell.get(1).getId())
                        .add(spell.get(2).getId())
                        .add(spell.get(3).getId())
                        .get()
        );
        characterClass.setStat(stat);
        db.getCharacterClassRepo().insert(characterClass);

        characterClass = new CharacterClass();
        stat = new CharacterStat();
        // Da default spell
        spell = initDruidSpell();

        // Warrior stats
        stat.health = 400;
        stat.mana = 200;

        stat.armor = 5;
        stat.magicResist = 5;

        stat.intellect = 0;
        stat.spirit = 0;
        stat.stamina = 0;
        stat.strength = 0;

        stat.speed = 0.04;

        // Warrior class
        characterClass.setCode("DRUID");
        characterClass.setName("Druid");

        characterClass.setSpells(
                new ArrayListBuilder<String>(4)
                        .add(spell.get(0).getId())
                        .add(spell.get(1).getId())
                        .add(spell.get(2).getId())
                        .add(spell.get(3).getId())
                        .get()
        );
        characterClass.setStat(stat);
        db.getCharacterClassRepo().insert(characterClass);
    }

    private static List<Spell> initRogueSpell() throws IOException {
        List<Spell> spells = new ArrayList<>();
        // QQQQQQQ
        Spell spell = new Spell();
        SpellEffect effect = new SpellEffect();
        CharacterStat stat = new CharacterStat();
        SpellCost cost = new SpellCost();

        // Spell cost
        cost.setAmount(10);
        cost.setType(SpellCost.CostType.MANA);

        // Spell stat
        stat.health = 70;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Stab");
        spell.setOrdinal(0);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(1)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(30.0);
        spell.setRadius(5.0);

        spell.setCooldown(30);
        spell.setCastTime(10);
        spell.setCost(cost);

        String id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // WWWWWW
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(20);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 50;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Shit");
        spell.setOrdinal(1);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(50.0);
        spell.setRadius(5.0);

        spell.setCooldown(30);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // EEEEEE
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(0);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.strength = 30;

        // Spell effect
        effect.setType(SpellEffect.SpellType.BUFF);
        effect.setDuration(100);
        effect.setStat(stat);

        // Spell
        spell.setName("Kussgec");
        spell.setOrdinal(2);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(180.0);
        spell.setRadius(1.0);

        spell.setCooldown(100);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));

        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(100);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 150;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Kick");
        spell.setOrdinal(3);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(2)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(90.0);
        spell.setRadius(5.0);

        spell.setCooldown(150);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        return spells;
    }

    private static List<Spell> initDruidSpell() throws IOException {
        List<Spell> spells = new ArrayList<>();
        // QQQQQQQ
        Spell spell = new Spell();
        SpellEffect effect = new SpellEffect();
        CharacterStat stat = new CharacterStat();
        SpellCost cost = new SpellCost();

        // Spell cost
        cost.setAmount(10);
        cost.setType(SpellCost.CostType.MANA);

        // Spell stat
        stat.health = 100;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.MAGIC);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Stab");
        spell.setOrdinal(0);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(1)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(15.0);
        spell.setRadius(12.0);

        spell.setCooldown(100);
        spell.setCastTime(10);
        spell.setCost(cost);

        String id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // WWWWWW
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(20);
        cost.setType(SpellCost.CostType.MANA);

        // Spell stat
        stat.health = 50;

        // Spell effect
        effect.setType(SpellEffect.SpellType.HEAL);
        effect.setStat(stat);

        // Spell
        spell.setName("Shit");
        spell.setOrdinal(1);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.HOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(30.0);
        spell.setRadius(1.0);

        spell.setCooldown(150);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // EEEEEE
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(0);
        cost.setType(SpellCost.CostType.MANA);

        // Spell stat
        stat.health = 50;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Kussgec");
        spell.setOrdinal(2);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(180.0);
        spell.setRadius(10.0);

        spell.setCooldown(10);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));

        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(100);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 100;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Kick");
        spell.setOrdinal(3);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(2)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(10.0);
        spell.setRadius(10.0);

        spell.setCooldown(50);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        return spells;
    }

    private static List<Spell> initWarriorSpell() throws IOException {
        List<Spell> spells = new ArrayList<>();
        // QQQQQQQ
        Spell spell = new Spell();
        SpellEffect effect = new SpellEffect();
        CharacterStat stat = new CharacterStat();
        SpellCost cost = new SpellCost();

        // Spell cost
        cost.setAmount(30);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 30;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Slam");
        spell.setOrdinal(0);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(2)
                        .add(SpellEffect.SpellType.BUFF)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(30.0);
        spell.setRadius(2.0);

        spell.setCooldown(100);
        spell.setCastTime(10);
        spell.setCost(cost);

        String id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // WWWWWW
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(0);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 50;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("Mega Slam");
        spell.setOrdinal(1);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(20.0);
        spell.setRadius(5.0);

        spell.setCooldown(100);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        // EEEEEE
        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(0);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.strength = 5;

        // Spell effect
        effect.setType(SpellEffect.SpellType.BUFF);
        effect.setStat(stat);

        // Spell
        spell.setName("Boostshit");
        spell.setOrdinal(2);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>()
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(180.0);
        spell.setRadius(1.0);

        spell.setCooldown(100);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));

        spell = new Spell();
        effect = new SpellEffect();
        stat = new CharacterStat();
        cost = new SpellCost();

        // Spell cost
        cost.setAmount(100);
        cost.setType(SpellCost.CostType.HEALTH);

        // Spell stat
        stat.health = 100;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("BIG SMASH");
        spell.setOrdinal(3);
        // This should be generated: spell.setDescription("");
        // also because of the upgrades (+ effects)

        spell.setEffect(effect);
        spell.setAcceptedUpgrades(
                new ArrayListBuilder<SpellEffect.SpellType>(2)
                        .add(SpellEffect.SpellType.BUFF)
                        .add(SpellEffect.SpellType.DOT)
                        .get()
        );
        spell.setMaxUpgrades(20);

        spell.setAlpha(30.0);
        spell.setRadius(10.0);

        spell.setCooldown(50);
        spell.setCastTime(10);
        spell.setCost(cost);

        id = db.getSpellRepo().insert(spell);
        spells.add(db.getSpellRepo().findById(id));
        return spells;
    }

    public void generateItems(int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            Item item = new Item();
            CharacterStat stat = new CharacterStat();

            Random r = new Random();

            stat.health = r.nextInt(10);
            stat.mana = r.nextInt(10);

            stat.armor = r.nextInt(10);
            stat.magicResist =  r.nextInt(10);

            stat.intellect = r.nextInt(10);
            stat.spirit = r.nextInt(10);
            stat.stamina = r.nextInt(10);
            stat.strength = r.nextInt(10);

            stat.speed = 0.0;

            item.setName("TestItem" + itemCounter++);
            item.setStat(stat);

            db.getItemRepo().insert(item);
        }
    }
    public static void cleanDBs(){
        NorocDB.getInstance().getCharacterRepo().deleteAll();
        NorocDB.getInstance().getItemRepo().deleteAll();
        NorocDB.getInstance().getUserRepo().deleteAll();
        NorocDB.getInstance().getSpellRepo().deleteAll();
        NorocDB.getInstance().getCharacterClassRepo().deleteAll();
        NorocDB.getInstance().getNpcRepo().deleteAll();
    }
}
