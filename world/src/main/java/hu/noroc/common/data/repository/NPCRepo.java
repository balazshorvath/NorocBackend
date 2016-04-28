package hu.noroc.common.data.repository;

import com.mongodb.DB;
import hu.noroc.common.mongodb.MongoDBRepo;
import hu.noroc.common.data.model.NPCModel;

/**
 * Created by Oryk on 4/1/2016.
 */
public class NPCRepo extends MongoDBRepo<NPCModel, String> {
    public NPCRepo(DB database) {
        super(database);
    }
}
