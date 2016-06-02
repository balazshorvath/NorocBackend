package hu.noroc.test;

import hu.noroc.entry.security.Compressor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.*;

/**
 * Created by Oryk on 4/20/2016.
 */
public class SecurityTest {
    @Test
    public void testCompression() throws IOException {
        String testdata = "TestData";

        String compressed = Compressor.gzip(testdata);

        assertEquals("H4sIAIleUFcA/wtJLS5xSSxJBAA2+6HACAAAAA==", compressed);

        String decompressed = Compressor.gunzip(compressed);
        assertEquals(testdata, decompressed);

    }
}
