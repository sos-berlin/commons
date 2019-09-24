
package com.sos.joc.model.joe.other;

import java.util.Date;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * folder content
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "folder")
@JsonPropertyOrder({
    "deliveryDate",
    "path",
    "folders",
    "jobs",
    "jobChains",
    "orders",
    "processClasses",
    "locks",
    "schedules",
    "monitors",
    "nodeParams",
    "others"
})
public class Folder {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    @JsonProperty("folders")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    private Set<String> folders = null;
    @JsonProperty("jobs")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    private Set<String> jobs = null;
    @JsonProperty("jobChains")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    private Set<String> jobChains = null;
    @JsonProperty("orders")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    private Set<String> orders = null;
    @JsonProperty("processClasses")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    private Set<String> processClasses = null;
    @JsonProperty("locks")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    private Set<String> locks = null;
    @JsonProperty("schedules")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    private Set<String> schedules = null;
    @JsonProperty("monitors")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    private Set<String> monitors = null;
    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("[jobChainName].config.xml files with node parameters")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    private Set<String> nodeParams = null;
    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     */
    @JsonProperty("others")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    private Set<String> others = null;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("folders")
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    public Set<String> getFolders() {
        return folders;
    }

    @JsonProperty("folders")
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    public void setFolders(Set<String> folders) {
        this.folders = folders;
    }

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public Set<String> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public void setJobs(Set<String> jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public Set<String> getJobChains() {
        return jobChains;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public void setJobChains(Set<String> jobChains) {
        this.jobChains = jobChains;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public Set<String> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public void setOrders(Set<String> orders) {
        this.orders = orders;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public Set<String> getProcessClasses() {
        return processClasses;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public void setProcessClasses(Set<String> processClasses) {
        this.processClasses = processClasses;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public Set<String> getLocks() {
        return locks;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public void setLocks(Set<String> locks) {
        this.locks = locks;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public Set<String> getSchedules() {
        return schedules;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public void setSchedules(Set<String> schedules) {
        this.schedules = schedules;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public Set<String> getMonitors() {
        return monitors;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(Set<String> monitors) {
        this.monitors = monitors;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public Set<String> getNodeParams() {
        return nodeParams;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public void setNodeParams(Set<String> nodeParams) {
        this.nodeParams = nodeParams;
    }

    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public Set<String> getOthers() {
        return others;
    }

    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public void setOthers(Set<String> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("path", path).append("folders", folders).append("jobs", jobs).append("jobChains", jobChains).append("orders", orders).append("processClasses", processClasses).append("locks", locks).append("schedules", schedules).append("monitors", monitors).append("nodeParams", nodeParams).append("others", others).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folders).append(processClasses).append(jobs).append(locks).append(path).append(schedules).append(jobChains).append(orders).append(deliveryDate).append(nodeParams).append(others).append(monitors).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Folder) == false) {
            return false;
        }
        Folder rhs = ((Folder) other);
        return new EqualsBuilder().append(folders, rhs.folders).append(processClasses, rhs.processClasses).append(jobs, rhs.jobs).append(locks, rhs.locks).append(path, rhs.path).append(schedules, rhs.schedules).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(deliveryDate, rhs.deliveryDate).append(nodeParams, rhs.nodeParams).append(others, rhs.others).append(monitors, rhs.monitors).isEquals();
    }

}
