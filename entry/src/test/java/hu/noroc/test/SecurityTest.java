package hu.noroc.test;

import hu.noroc.entry.security.Compressor;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Oryk on 4/20/2016.
 */
public class SecurityTest {
    @Test
    public void testCompression() throws IOException {
        String testdata = "TestData";

        String compressed = Compressor.gzip(testdata);

        String decompressed = Compressor.gunzip(compressed);
        assertEquals(testdata, decompressed);

    }
}
