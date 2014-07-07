package com.sos.resources;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A class to get common resources in various types.
 *
 * The static methods of this class are for access to resources. You can either get a resource via a symbolic name defined in the
 * enum class SOSResource or by giving a full resource name (String) to the access method.
 *
 * @version 1.0
 * @author Stefan Schädlich
 */
public class SOSResourceFactory {

    private static Logger logger = LoggerFactory.getLogger(SOSResourceFactory.class);

    public static URL asURL(SOSResource forResource) {
        logger.info("Try to read resource " + forResource.getFullName());
        return Resources.getResource(forResource.getFullName());
    }

    public static URL asURL(String forResource) {
        logger.info("Try to read resource " + forResource);
        return Resources.getResource(normalizePackageName(forResource));
    }

    public static InputStream asInputStream(SOSResource forResource) {
        try {
            return asURL(forResource).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream asInputStream(String forResource) {
        try {
            return asURL(forResource).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StreamSource asStreamSource(SOSResource forResource) {
        return new StreamSource(asInputStream(forResource));
    }

    public static StreamSource asStreamSource(String forResource) {
        return new StreamSource(asInputStream(forResource));
    }

    public static File asFile(String forResource) {
        URL url = asURL(forResource);
        File resultFile = null;
        try {
            resultFile = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return resultFile;
    }

    private static String normalizePackageName(String forResource) {
        return (forResource.startsWith("/")) ? forResource.substring(1) : forResource;
    }

}
