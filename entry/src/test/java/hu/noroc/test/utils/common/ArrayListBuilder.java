package hu.noroc.test.utils.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oryk on 4/19/2016.
 */
public class ArrayListBuilder<T> {
    List<T> list;

    public ArrayListBuilder() {
        this.list = new ArrayList<>();
    }
    public ArrayListBuilder(int size) {
        this.list = new ArrayList<>(size);
    }

    public ArrayListBuilder<T> add(T t){
        this.list.add(t);
        return this;
    }
    public ArrayListBuilder<T> addAll(List<T> t){
        this.list.addAll(t);
        return this;
    }
    public List<T> get(){
        return this.list;
    }
}
