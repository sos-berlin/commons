package com.sos.resources;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by schaedi on 10.07.2014.
 */
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
