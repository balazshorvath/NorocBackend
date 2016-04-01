package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.user.Session;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Oryk on 1/11/2016.
 */
public class SessionRepo extends MongoDBRepo<Session, Long> {
    public SessionRepo(MongoDatabase database) {
        super(database);
    }

    public Session isValid(String session) throws IOException {
        Document query = new Document("sessionId", session);
        Document doc = collection.find(query).first();
        if(doc == null)
            return null;
        Session session1 = new ObjectMapper().readValue(doc.toJson(), Session.class);
        if(session1.getLoginDate().getTime() < (new Date().getTime() + 60 * 60 * 24))
            return session1;
        collection.deleteOne(query);
        return null;
    }
}