package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.user.User;
import hu.noroc.common.mongodb.MongoDBRepo;


/**
 * Created by Oryk on 12/23/2015.
 */
public class UserRepo extends MongoDBRepo<User, Long> {
    public UserRepo(MongoDatabase database) {
        super(database);
    }
}
