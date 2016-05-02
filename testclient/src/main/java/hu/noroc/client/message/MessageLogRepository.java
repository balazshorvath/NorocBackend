package hu.noroc.client.message;

import com.mongodb.DB;
import hu.noroc.client.message.log.MessageLog;
import hu.noroc.common.mongodb.MongoDBRepo;

/**
 * Created by Oryk on 4/29/2016.
 */
public class MessageLogRepository extends MongoDBRepo<MessageLog, String> {
    public MessageLogRepository(DB database) {
        super(database);
    }
}
