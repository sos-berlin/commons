package com.sos.resources;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import com.google.common.io.Resources;

public class ResourceHelperTest {

    @Test
    public void test() {
        URL url = Resources.getResource("com/sos/resources/test.txt");
        File tempFile = ResourceHelper.getInstance().createFileFromURL(url);
        File tempDir = ResourceHelper.getInstance().getWorkingDirectory();
        assertTrue(tempFile.exists());
        assertTrue(tempDir.exists());
        ResourceHelper.destroy();
        assertFalse(tempFile.exists());
        assertFalse(tempDir.exists());
    }

}