package com.sos.resources;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** A list of common resources used in different products/projects.
 *
 * The list entries should refer to the newest version of a resource file. */
public enum SOSProductionResource implements SOSResource {

    JOB_CHAIN_EXTENSIONS_XSD(basePackage() + "/xsd", "job-chain-extensions-v1.0.xsd"),
    JOB_DOC_XSD(basePackage() + "/xsd", "scheduler_job_documentation_v1.2.xsd"),
    JOB_DOC_XSLT(basePackage() + "/xsl/jobdoc", "scheduler_job_documentation_v1.1.xsl"),
    EVENT_SERVICE_XSD(basePackage() + "/xsd", "events2actions.xsd"),
    // the version is determined via the dependency setting in the pom.xml
    SCHEDULER_XSD("com/sos/scheduler/enginedoc/common", "scheduler.xsd");

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSProductionResource.class);
    private final String packageName;
    private final String resourceName;

    private SOSProductionResource(String packageName, String resourceName) {
        this.packageName = packageName + "/";
        this.resourceName = resourceName;
    }

    public static String basePackage() {
        return SOSProductionResource.class.getPackage().getName().replaceAll("\\.", "/");
    }

    @Override
    public String getFullName() {
        return packageName + resourceName;
    }

    public StreamSource getAsStreamSource() {
        return new StreamSource(getInputStream4Resource());
    }

    public InputStream getInputStream4Resource() {
        String pstrKey = "/" + getFullName();
        InputStream objSS = this.getClass().getResourceAsStream(pstrKey);
        if (objSS == null) {
            throw new RuntimeException("Resource not found: " + pstrKey);
        }
        return objSS;
    }

    public URL getURL() {
        URL objU = null;
        try {
            objU = new URL(packageName + resourceName);
        } catch (MalformedURLException e) {
            LOGGER.error("", e);
        }
        return objU;
    }

}
