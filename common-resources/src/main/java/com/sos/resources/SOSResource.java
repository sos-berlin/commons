package com.sos.resources;

/**
 * A list of common resources used in different projects.
 *
 * The list entries should refer to the newest version of a resource file.
 */
public enum SOSResource {

    JOB_CHAIN_EXTENSIONS_XSD(basePackage() + "/xsd","job-chain-extensions-v1.0.xsd"),

    // the version is determined via the dependency setting in the pom.xml
    SCHEDULER_XSD("com/sos/scheduler/enginedoc/common","scheduler.xsd");

    private final String packageName;
    private final String resourceName;

    private SOSResource(String packageName, String resourceName) {
        this.packageName = packageName + "/";
        this.resourceName = resourceName;
    }

    private static String basePackage() {
        return SOSResource.class.getPackage().getName().replaceAll("\\.","/");
    }

    public String getFullName() {
        return packageName + resourceName;
    }
}
