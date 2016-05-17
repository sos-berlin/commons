package com.sos.resources;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/** A class to get common resources in various types.
 *
 * The static methods of this class are for access to resources. You can either
 * get a resource via a symbolic name defined in the enum class
 * SOSProductionResource or by giving a full resource name (String) to the
 * access method.
 *
 * @version 1.0
 * @author Stefan Schädlich */
public class SOSResourceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSResourceFactory.class);

    public static URL asURL(SOSResource forResource) {
        LOGGER.info("Try to read resource {}.", forResource.getFullName());
        return Resources.getResource(forResource.getFullName());
    }

    public static URL asURL(String forResource) {
        LOGGER.info("Try to read resource {}.", forResource);
        return Resources.getResource(normalizePackageName(forResource));
    }

    public static InputStream asInputStream(SOSResource forResource) {
        try {
            return asURL(forResource).openStream();
        } catch (IOException e) {
            LOGGER.error("Error reading resource {}.", forResource.getFullName(), e);
            throw new RuntimeException(e);
        }
    }

    public static InputStream asInputStream(String forResource) {
        try {
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

    public static File asFile(SOSResource forResource) {
        URL url = asURL(forResource);
        return ResourceHelper.getInstance().createFileFromURL(url);
    }

    public static File asFile(String forResource) {
        URL url = asURL(forResource);
        return ResourceHelper.getInstance().createFileFromURL(url);
    }

    public static void removeTemporaryFiles() {
        ResourceHelper.destroy();
    }

    private static String normalizePackageName(String forResource) {
        return (forResource.startsWith("/")) ? forResource.substring(1) : forResource;
    }

}