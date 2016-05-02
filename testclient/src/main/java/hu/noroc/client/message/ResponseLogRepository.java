package hu.noroc.client.message;

import com.mongodb.DB;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/29/2016.
 */
public class ResponseLogRepository extends MongoDBRepo<ResponseLog, String> {
    public ResponseLogRepository(DB database) {
        super(database);
    }
}
