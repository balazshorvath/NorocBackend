package hu.noroc.common.mongodb;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oryk on 11/26/2015.
 */
public interface Repository<T, ID> {
    T findById(ID id) throws IOException;
    List<T> findBy(String key, String value) throws IOException;
    List<T> findAll() throws IOException;

    ID insert(T t) throws IOException;
    ID save(T t) throws IOException;
    void delete(ID id);
    void deleteAll();
}
