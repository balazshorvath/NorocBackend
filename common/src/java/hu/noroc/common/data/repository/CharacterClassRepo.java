package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/1/2016.
 */
public class CharacterClassRepo extends MongoDBRepo<CharacterClass, String> {
    public CharacterClassRepo(MongoDatabase database) {
        super(database);
    }
}
