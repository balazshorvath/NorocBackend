package hu.noroc.common.data.repository;


import com.mongodb.DB;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.mongodb.MongoDBRepo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oryk on 1/11/2016.
 */
public class CharacterRepo extends MongoDBRepo<PlayerCharacter, String> {
    public CharacterRepo(DB database) {
        super(database);
    }

    public List<PlayerCharacter> findByUser(String value) throws IOException {
        return collection.find().is("userId", value).toArray();
    }
}
