package hu.noroc.common.data.repository;


import com.mongodb.client.MongoDatabase;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 1/11/2016.
 */
public class CharacterRepo extends MongoDBRepo<Character, Long> {
    public CharacterRepo(MongoDatabase database) {
        super(database);
    }

    public List<Character> findByUser(Long value) {
        List<Character> res = new ArrayList<>();
        for(Document d : collection.find(new Document("userId", value))){
            res.add(jsonBuilder.create().fromJson(d.toJson(), documentClass));
        }
        return res;
    }
}
