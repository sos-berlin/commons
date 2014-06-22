package com.sos.resources;

/**
 * A list of common resources used in different products/projects.
 *
 * The list entries should refer to the newest version of a resource file.
 */
public enum SOSResource {

    JOB_CHAIN_EXTENSIONS_XSD(basePackage() + "/xsd","job-chain-extensions-v1.0.xsd"),

    JOB_DOC_XSD(basePackage() + "/xsd","scheduler_job_documentation_v1.1.xsd"),

    EVENT_SERVICE_XSD(basePackage() + "/xsd","events2actions.xsd"),

    // the version is determined via the dependency setting in the pom.xml
    SCHEDULER_XSD("com/sos/scheduler/enginedoc/common","scheduler.xsd");

    private final String packageName;
    private final String resourceName;

    private SOSResource(String packageName, String resourceName) {
        this.packageName = packageName + "/";
        this.resourceName = resourceName;
    }

    public static String basePackage() {
        return SOSResource.class.getPackage().getName().replaceAll("\\.","/");
    }

    public String getFullName() {
        return packageName + resourceName;
    }
}
