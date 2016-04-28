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
                        MessageDigest.getInstance("SHA-256").digest("password".getBytes())
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

        character.setX(0.0);
        character.setY(0.0);
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
        characterCounter++;

        return db.getCharacterRepo().findById(id);
    }

    public static void initClasses() throws IOException {
        CharacterClass characterClass = new CharacterClass();
        CharacterStat stat = new CharacterStat();
        // Da default spell
        Spell spell = initWarriorSpell();

        // Warrior stats
        stat.health = 500;
        stat.mana = 200;

        stat.armor = 10;
        stat.magicResist = 0;

        stat.intellect = 0;
        stat.spirit = 0;
        stat.stamina = 0;
        stat.strength = 0;

        stat.speed = 1.0;

        // Warrior class
        characterClass.setCode("WARRIOR");
        characterClass.setName("Warrior");

        characterClass.setSpells(
                new ArrayListBuilder<String>(1)
                        .add(spell.getId())
                        .get()
        );
        characterClass.setStat(stat);
        db.getCharacterClassRepo().insert(characterClass);
    }

    private static Spell initWarriorSpell() throws IOException {
        Spell spell = new Spell();
        SpellEffect effect = new SpellEffect();
        CharacterStat stat = new CharacterStat();
        SpellCost cost = new SpellCost();

        // Spell cost
        cost.setAmount(100);
        cost.setType(SpellCost.CostType.MANA);

        // Spell stat
        stat.health = 100;

        // Spell effect
        effect.setDamageType(SpellEffect.DamageType.PHYSICAL);
        effect.setType(SpellEffect.SpellType.DAMAGE);
        effect.setStat(stat);

        // Spell
        spell.setName("TestSpell");
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

        spell.setCooldown(1000);
        spell.setCastTime(0);
        spell.setCost(cost);

        String id = db.getSpellRepo().insert(spell);
        return db.getSpellRepo().findById(id);
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
