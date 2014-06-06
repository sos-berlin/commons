package com.sos.resources;

/**
 * A list of common resources used in different projects.
 *
 * The list entries should refer to the newest version of a resource file.
 */
public enum SOSResource {

    XSD_JOB_CHAIN_EXTENSIONS("xsd","job-chain-extensions-v1.0.xsd");

    private final static String BASE_PACKAGE = "com/sos/resources/";

    private final String packageName;
    private final String resourceName;

    private SOSResource(String subPackage, String resourceName) {
        this.packageName = BASE_PACKAGE + subPackage + "/";
        this.resourceName = resourceName;
    }

    public String getFullName() {
        return packageName + resourceName;
    }
}
