package hu.noroc.common.data.repository;

import com.mongodb.DB;
import hu.noroc.common.mongodb.MongoDBRepo;
import hu.noroc.common.data.model.Item;

/**
 * Created by Oryk on 4/1/2016.
 */
public class ItemRepo extends MongoDBRepo<Item, String> {
    public ItemRepo(DB database) {
        super(database);
    }
}
