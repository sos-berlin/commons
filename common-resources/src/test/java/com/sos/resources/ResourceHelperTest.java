package com.sos.resources;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class ResourceHelperTest {

    @Test
    public void test() {
        URL url = SOSResourceFactory.asURL("com/sos/resources/test.txt");
        Path tempFile = ResourceHelper.getInstance().createFileFromURL(url);
        Path tempDir = ResourceHelper.getInstance().getWorkingDirectory();
        assertTrue(Files.exists(tempFile));
        assertTrue(Files.exists(tempDir));
    }

}