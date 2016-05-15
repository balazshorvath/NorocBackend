package hu.noroc.common.communication.request.pregame;

import hu.noroc.common.communication.request.Request;

/**
 * Created by Oryk on 2016. 05. 15..
 */
public class CreateCharacterRequest extends Request {
    private String name;
    private String classId;

    public CreateCharacterRequest() {
        super.type = "CreateCharacterRequest";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
