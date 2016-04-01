package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/1/2016.
 */
public class SpellRepo extends MongoDBRepo<Spell, String> {

    public SpellRepo(MongoDatabase database) {
        super(database);
    }
}
