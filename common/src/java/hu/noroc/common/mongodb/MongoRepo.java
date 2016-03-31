package hu.noroc.common.mongodb;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oryk on 11/26/2015.
 */
public interface MongoRepo<T, ID> {
    T findById(ID id) throws IOException;
    List<T> findBy(String key, String value) throws IOException;

    void insert(T t) throws IOException;
    void delete(ID id);

}
