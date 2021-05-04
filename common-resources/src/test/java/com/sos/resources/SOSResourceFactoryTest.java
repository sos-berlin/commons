package com.sos.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class SOSResourceFactoryTest {

    @Test
    public void testAsFile() throws IOException {
        Path f = SOSResourceFactory.asFile("com/sos/resources/test.txt");
        assertEquals("this file contains only dummy text.", new String(Files.readAllBytes(f), Charset.defaultCharset()));
    }

    @Test
    public void testNamedResource() {
        Path tempFile = SOSResourceFactory.asFile(SOSProductionResource.SCHEDULER_XSD);
        assertTrue(Files.exists(tempFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsFileInvalid() throws IOException {
        SOSResourceFactory.asFile("com/sos/resources/invalid.txt");
    }

}
