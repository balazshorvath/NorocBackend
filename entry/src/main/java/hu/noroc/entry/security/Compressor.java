package hu.noroc.entry.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Oryk on 4/7/2016.
 */
public class Compressor {

    public static String gzip(String data){
        ByteArrayOutputStream result;
        GZIPOutputStream gzip = null;
        try {
            result = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(result);

            gzip.write(data.getBytes());

            return result.toString();
        } catch (IOException e) {
            //TODO: log
            return null;
        } finally {
            try {
                if (gzip != null)
                    gzip.close();
            } catch (IOException e) {
            }
        }
    }
    public static String gunzip(String data){
        GZIPInputStream gzip = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
            gzip = new GZIPInputStream(inputStream);
            InputStreamReader reader = new InputStreamReader(gzip);

            char[] buffer = new char[256];
            String result = "";
            while(reader.read(buffer) > 0){
                result += new String(buffer);
            }
            return result;
        } catch (IOException e) {
            //TODO: log
            return null;
        } finally {
            try {
                if (gzip != null)
                    gzip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
