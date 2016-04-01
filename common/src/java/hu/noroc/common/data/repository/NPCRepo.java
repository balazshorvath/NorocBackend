package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.NPCModel;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/1/2016.
 */
public class NPCRepo extends MongoDBRepo<NPCModel, String> {
    public NPCRepo(MongoDatabase database) {
        super(database);
    }
}
