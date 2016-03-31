package hu.noroc.common.data.model;

import org.bson.types.ObjectId;

/**
 * Created by Oryk on 1/11/2016.
 */
public class Character {
    private String id = new ObjectId().toString();
    private String name;
    private Long userId;
    private Long classId;
    private Long xp = new Long(0);

    public Character() {
    }

    public Character(String name, Long userId, Long classId) {
        this.name = name;
        this.userId = userId;
        this.classId = classId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }
}
