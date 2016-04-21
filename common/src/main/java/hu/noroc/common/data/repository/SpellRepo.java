package hu.noroc.common.data.repository;

import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.spell.Spell;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.types.ObjectId;

/**
 * Created by Oryk on 4/1/2016.
 */
public class SpellRepo extends MongoDBRepo<Spell, String> {

    public SpellRepo(DB database) {
        super(database);
    }
}
