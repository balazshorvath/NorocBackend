package hu.noroc.common.data.repository;

import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.model.user.User;
import hu.noroc.common.mongodb.MongoDBRepo;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Oryk on 12/23/2015.
 */
public class UserRepo extends MongoDBRepo<User, Long> {
    public UserRepo(MongoDatabase database) {
        super(database);
    }

    public User login(String username, String password){
        String hash;
        try {
            hash = new String(MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            return null;
        }
        Document document = collection.find(new Document("username", username).append("password", hash)).first();

        try {
            return document != null ? new ObjectMapper().readValue(document.toJson(), User.class) : null;
        } catch (IOException e) {
            return null;
        }
    }
}
