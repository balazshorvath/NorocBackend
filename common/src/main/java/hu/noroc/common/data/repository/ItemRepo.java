package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.Item;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/1/2016.
 */
public class ItemRepo extends MongoDBRepo<Item, String> {
    public ItemRepo(MongoDatabase database) {
        super(database);
    }
}
