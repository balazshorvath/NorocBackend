package hu.noroc.common.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.jodah.typetools.TypeResolver;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Oryk on 11/26/2015.
 */
public class MongoDBRepo<T, ID> implements MongoRepo<T, ID> {
    protected MongoCollection<Document> collection;
    protected ObjectMapper json = new ObjectMapper();
    protected final Class<T> documentClass;
    protected final Class<ID> indexClass;

    public MongoDBRepo(MongoDatabase database){
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(MongoDBRepo.class, getClass());
        this.documentClass = (Class<T>) typeArguments[0];
        this.indexClass = (Class<ID>) typeArguments[1];

        String[] tmp = documentClass.getName().split("\\.");
        String name = tmp[tmp.length - 1];

        collection = database.getCollection(name);

        if(collection == null) {
            database.createCollection(name);
            collection = database.getCollection(name);
        }
    }

    @Override
    public T findById(ID id) throws IOException {
        Document tmp = collection.find(new Document("id", id)).first();
        return json.readValue(tmp.toJson(), documentClass);
    }

    @Override
    public List<T> findBy(String key, String value) throws IOException {
        List<T> res = new ArrayList<>();
        for(Document d : collection.find(new Document(key, value))){
            res.add(json.readValue(d.toJson(), documentClass));
        }
        return res;
    }

    @Override
    public void insert(T t) throws IOException {
        collection.insertOne(Document.parse(json.writeValueAsString(t)));
    }

    @Override
    public void delete(ID id) {
        collection.deleteOne(new Document("id", id));
    }
}
