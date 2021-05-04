package com.sos.resources;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A singleton to deal with resources.
 *
 * At end the method destroy() should be executed, to remove the temporary files
 * holding the resources. */
public class ResourceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHelper.class);
    private static Path workingDirectory = null;
    private static ResourceHelper instance = null;

    private ResourceHelper() {
        //
    }

    public static ResourceHelper getInstance() {
        if (instance == null) {
            instance = new ResourceHelper();
        }
        return instance;
    }

    /** Creates a file from content of the given resource resides in the
     * workingDirectory.
     * 
     * @param resource
     * @return */
    public Path createFileFromURL(URL resource) {
        LOGGER.info("Resource is {}.", resource.getPath());
        try {
            String filename = resource.getPath().replaceFirst(".*/([^/]+)$", "$1");
            createWorkingDirectory();
            Path configFile = workingDirectory.resolve(filename);
            LOGGER.info(String.format("Copy resource %s to %s", resource.getPath(), configFile.toString()));
            Files.copy(resource.openStream(), configFile, StandardCopyOption.REPLACE_EXISTING);
            return configFile;
        } catch (Exception e) {
            LOGGER.error("Error reading resource {}.", resource.getPath(), e);
            throw new RuntimeException(e);
        }
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public Path createWorkingDirectory() throws IOException {
        if (workingDirectory == null) {
            workingDirectory = Files.createTempDirectory(null);
        }
        return workingDirectory;
    }
    
    public static void destroy() {
        if (Files.exists(workingDirectory)) {
            try {
                Files.walk(workingDirectory).sorted(Comparator.reverseOrder()).forEach(entry -> {
                    try {
                        Files.delete(entry);
                    } catch (IOException e) {
                        throw new RuntimeException(String.format("%1$s could not be removed: %2$s", entry.toString(), e.toString()), e);
                    }
                });
            } catch (IOException e) {
                LOGGER.error("Error at delete resource {}.", workingDirectory, e);
            }
        }
    }
}
