package hu.noroc.common.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.jodah.typetools.TypeResolver;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongojack.JacksonDBCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Oryk on 11/26/2015.
 */
public class MongoDBRepo<T, ID> implements MongoRepo<T, ID> {
    protected JacksonDBCollection<T, ID> collection;
    protected ObjectMapper json = new ObjectMapper();
    protected final Class<T> documentClass;
    protected final Class<ID> indexClass;

    public MongoDBRepo(DB database){
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(MongoDBRepo.class, getClass());
        this.documentClass = (Class<T>) typeArguments[0];
        this.indexClass = (Class<ID>) typeArguments[1];

        String[] tmp = documentClass.getName().split("\\.");
        String name = tmp[tmp.length - 1];

        collection = JacksonDBCollection.wrap(
                database.getCollection(documentClass.getSimpleName()),
                documentClass,
                indexClass
        );

    }

    @Override
    public T findById(ID id) throws IOException {
        return collection.findOneById(id);
    }

    @Override
    public List<T> findBy(String key, String value) throws IOException {
        return collection.find().is(key, value).toArray();
    }

    @Override
    public List<T> findAll() throws IOException {
        return collection.find().toArray();
    }

    @Override
    public ID insert(T t) throws IOException {
        return collection.insert(t).getSavedId();
    }

    @Override
    public void delete(ID id) {
        collection.removeById(id);
    }
}
