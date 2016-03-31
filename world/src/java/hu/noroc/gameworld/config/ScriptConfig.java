package hu.noroc.gameworld.config;

import java.util.Map;

/**
 * Created by Oryk on 3/20/2016.
 */
public class ScriptConfig {
    private String file;
    private Map<String, String> params;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
