package hu.noroc.common.data.repository;


import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
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
