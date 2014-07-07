package com.sos.resources;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class SOSResourceFactoryTest {

    @Test
    public void testAsFile() throws IOException {
        File f = SOSResourceFactory.asFile("/com/sos/resources/test.txt");
        assertEquals("this file contains only dummy text.", Files.toString(f, Charset.defaultCharset()));
    }

}
