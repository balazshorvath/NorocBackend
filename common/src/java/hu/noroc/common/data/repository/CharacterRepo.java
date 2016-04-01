package hu.noroc.common.data.repository;


import com.mongodb.client.MongoDatabase;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.Document;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 1/11/2016.
 */
public class CharacterRepo extends MongoDBRepo<Character, String> {
    public CharacterRepo(MongoDatabase database) {
        super(database);
    }

    public List<Character> findByUser(String value) throws IOException {
        List<Character> res = new ArrayList<>();
        for(Document d : collection.find(new Document("userId", value))){
            res.add(new ObjectMapper().readValue(d.toJson(), documentClass));
        }
        return res;
    }
}
