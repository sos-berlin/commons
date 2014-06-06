package com.sos.resources;

import com.google.common.io.Resources;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A class to get common resources in various types.
 */
public class SOSResourceFactory {

    public URL asURL(SOSResource forResource) {
        return Resources.getResource(forResource.getFullName());
    }

    public InputStream asInputStream(SOSResource forResource) {
        try {
            return asURL(forResource).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StreamSource asStreamSource(SOSResource forResource) {
        return new StreamSource(asInputStream(forResource));
    }

}
