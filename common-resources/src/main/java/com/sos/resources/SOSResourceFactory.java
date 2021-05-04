package com.sos.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.io.Resources;

/** A class to get common resources in various types.
 *
 * The static methods of this class are for access to resources. You can either
 * get a resource via a symbolic name defined in the enum class
 * SOSProductionResource or by giving a full resource name (String) to the
 * access method.
 *
 */
public class SOSResourceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSResourceFactory.class);

    public static URL asURL(SOSResource forResource) {
        return asURL(forResource.getFullName());
    }

    public static URL asURL(String forResource) {
        LOGGER.info("Try to read resource {}.", forResource);
        URL url = SOSResourceFactory.class.getClassLoader().getResource(normalizePackageName(forResource));
        if (url == null) {
            throw new IllegalArgumentException("resource '" + forResource + "is unreadable");
        }
        return url;
    }

    public static InputStream asInputStream(SOSResource forResource) {
        return asInputStream(forResource.getFullName());
    }

    public static InputStream asInputStream(String forResource) {
        try {
//            InputStream in =  SOSResourceFactory.class.getClassLoader().getResourceAsStream(forResource);
//            if (in == null) {
//                throw new IOException("resource stream of '" + forResource + "is unreadable");
//            }
//            return in;
            return asURL(forResource).openStream();
        } catch (IOException e) {
            LOGGER.error("Error reading resource {}.", forResource, e);
            throw new RuntimeException(e);
        }
    }

    public static StreamSource asStreamSource(SOSResource forResource) {
        return new StreamSource(asInputStream(forResource));
    }

    public static StreamSource asStreamSource(String forResource) {
        return new StreamSource(asInputStream(forResource));
    }

    public static Path asFile(SOSResource forResource) {
        return ResourceHelper.getInstance().createFileFromURL(asURL(forResource));
    }

    public static Path asFile(String forResource) {
        return ResourceHelper.getInstance().createFileFromURL(asURL(forResource));
    }

    private static String normalizePackageName(String forResource) {
        return (forResource.startsWith("/")) ? forResource.substring(1) : forResource;
    }
}