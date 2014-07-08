package com.sos.resources;

/**
 * A list of common test resources used in different products/projects.
 *
 * The list entries should refer to the newest version of a resource file.
 */
public enum SOSTestResource implements SOSResource {
    HIBERNATE_CONFIGURATION_DEFAULT(basePackage() + "/hibernate","hibernate.cfg.xml"),
    HIBERNATE_CONFIGURATION_ORACLE(basePackage() + "/hibernate","hibernate_oracle.cfg.xml"),
    HIBERNATE_CONFIGURATION_POSTGRES(basePackage() + "/hibernate","hibernate_pgsql.cfg.xml"),
    SHIRO_CONFIGURATION_DEFAULT(basePackage() + "/shiro","shiro.ini"),
;

    private final String packageName;
    private final String resourceName;

    private SOSTestResource(String packageName, String resourceName) {
        this.packageName = packageName + "/";
        this.resourceName = resourceName;
    }

    public static String basePackage() {
        return SOSTestResource.class.getPackage().getName().replaceAll("\\.","/");
    }

    public String getFullName() {
        return packageName + resourceName;
    }
}
