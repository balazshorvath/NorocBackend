package hu.noroc.entry.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oryk on 4/7/2016.
 */
public class EntryConfig {
    private static Map<String, String> values;

    public static void loadConfig(String file) throws IOException {
        values = (new ObjectMapper()).readValue(new File(file), new TypeReference<HashMap<String, String>>(){});
    }

    public static String getValue(String key) {
        return values.get(key);
    }
}
