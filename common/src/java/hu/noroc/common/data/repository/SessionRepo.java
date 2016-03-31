package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import com.noroc.ndbc.data.model.Session;
import com.noroc.ndbc.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 1/11/2016.
 */
public class SessionRepo extends MongoDBRepo<Session, Long> {
    public SessionRepo(MongoDatabase database) {
        super(database);
    }
}