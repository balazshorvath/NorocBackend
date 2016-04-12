package hu.noroc.common.data.repository;


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
    public CharacterRepo(MongoDatabase database) {
        super(database);
    }

    public List<PlayerCharacter> findByUser(String value) throws IOException {
        List<PlayerCharacter> res = new ArrayList<>();
        for(Document d : collection.find(new Document("userId", value))){
            res.add(new ObjectMapper().readValue(d.toJson(), documentClass));
        }
        return res;
    }
}
