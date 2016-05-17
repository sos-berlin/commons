package com.sos.resources;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/** A singleton to deal with resources.
 *
 * At end the method destroy() should be executed, to remove the temporary files
 * holding the resources. */
public class ResourceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHelper.class);
    private static File workingDirectory = null;
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
    public File createFileFromURL(URL resource) {
        LOGGER.info("Resource is {}.", resource.getPath());
        String[] arr = resource.getPath().split("/");
        String filenameWithoutPath = arr[arr.length - 1];
        File configFile = null;
        try {
            String fileContent = Resources.toString(resource, Charset.defaultCharset());
            configFile = createFileFromString(fileContent, filenameWithoutPath);
        } catch (IOException e) {
            LOGGER.error("Error reading resource {}.", resource.getPath(), e);
            throw new RuntimeException(e);
        }
        return configFile;
    }

    /** Create a file with given fileContent in a file named filenameWithoutPath
     * (resides in the workingDirectory).
     * 
     * @param fileContent
     * @param filenameWithoutPath
     * @return */
    private File createFileFromString(String fileContent, String filenameWithoutPath) {
        File configFile = null;
        createWorkingDirectory();
        LOGGER.info("Copy resource to folder {}.", workingDirectory.getAbsolutePath());
        LOGGER.info("Targetname is {}.", filenameWithoutPath);
        try {
            workingDirectory.mkdirs();
            LOGGER.info("Create file from Resource String:\n{},", fileContent);
            configFile = new File(workingDirectory, filenameWithoutPath);
            LOGGER.info("Write file {}.", configFile.getAbsolutePath());
            Files.write(fileContent, configFile, Charset.defaultCharset());
        } catch (IOException e) {
            LOGGER.error("Could not create File from resource String:\n{}", fileContent);
            throw new RuntimeException(e);
        }
        return configFile;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public static void destroy() {
        removeFolderRecursively(workingDirectory);
    }

    private static void removeFolderRecursively(File folder) {
        if (workingDirectory != null && workingDirectory.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    deleteFile(f);
                } else {
                    removeFolderRecursively(f);
                }
            }
            deleteFile(folder);
        }
    }

    private static void deleteFile(File f) {
        String type = (f.isDirectory()) ? "folder" : "file";
        if (f.delete()) {
            LOGGER.info("Temporary {} [{}] removed succesfully.", type, f.getAbsolutePath());
        } else {
            LOGGER.warn("Could not delete Temporary {} [{}].", type, f.getAbsolutePath());
        }
    }

    public File createWorkingDirectory() {
        if (workingDirectory == null) {
            workingDirectory = Files.createTempDir();
        }
        return workingDirectory;
    }
}
