package hu.noroc.common.data.repository;

import com.mongodb.DB;
import hu.noroc.common.mongodb.MongoDBRepo;
import hu.noroc.common.data.model.user.User;
import org.mongojack.DBQuery;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * Created by Oryk on 12/23/2015.
 */
public class UserRepo extends MongoDBRepo<User, String> {
    public UserRepo(DB database) {
        super(database);
    }

    public User login(String username, String password){
        String hash;
        try {
            hash = new String(Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(password.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return collection.findOne(DBQuery.and(
                DBQuery.is("username", username),
                DBQuery.is("password", hash)
        ));
    }
}
