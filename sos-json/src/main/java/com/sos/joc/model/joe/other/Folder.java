
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
    "jobs",
    "jobChains",
    "orders",
    "agentClusters",
    "processClasses",
    "locks",
    "schedules",
    "monitors"
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
    @JsonProperty("jobs")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    private Set<FolderItem> jobs = null;
    @JsonProperty("jobChains")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    private Set<FolderItem> jobChains = null;
    @JsonProperty("orders")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    private Set<FolderItem> orders = null;
    @JsonProperty("agentClusters")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "agent_clusters", isAttribute = false)
    private Set<FolderItem> agentClusters = null;
    @JsonProperty("processClasses")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    private Set<FolderItem> processClasses = null;
    @JsonProperty("locks")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    private Set<FolderItem> locks = null;
    @JsonProperty("schedules")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    private Set<FolderItem> schedules = null;
    @JsonProperty("monitors")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    private Set<FolderItem> monitors = null;

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

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public Set<FolderItem> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public void setJobs(Set<FolderItem> jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public Set<FolderItem> getJobChains() {
        return jobChains;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public void setJobChains(Set<FolderItem> jobChains) {
        this.jobChains = jobChains;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public Set<FolderItem> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public void setOrders(Set<FolderItem> orders) {
        this.orders = orders;
    }

    @JsonProperty("agentClusters")
    @JacksonXmlProperty(localName = "agent_clusters", isAttribute = false)
    public Set<FolderItem> getAgentClusters() {
        return agentClusters;
    }

    @JsonProperty("agentClusters")
    @JacksonXmlProperty(localName = "agent_clusters", isAttribute = false)
    public void setAgentClusters(Set<FolderItem> agentClusters) {
        this.agentClusters = agentClusters;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public Set<FolderItem> getProcessClasses() {
        return processClasses;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public void setProcessClasses(Set<FolderItem> processClasses) {
        this.processClasses = processClasses;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public Set<FolderItem> getLocks() {
        return locks;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public void setLocks(Set<FolderItem> locks) {
        this.locks = locks;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public Set<FolderItem> getSchedules() {
        return schedules;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public void setSchedules(Set<FolderItem> schedules) {
        this.schedules = schedules;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public Set<FolderItem> getMonitors() {
        return monitors;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(Set<FolderItem> monitors) {
        this.monitors = monitors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("path", path).append("jobs", jobs).append("jobChains", jobChains).append("orders", orders).append("agentClusters", agentClusters).append("processClasses", processClasses).append("locks", locks).append("schedules", schedules).append("monitors", monitors).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(agentClusters).append(processClasses).append(jobs).append(schedules).append(jobChains).append(orders).append(deliveryDate).append(locks).append(monitors).toHashCode();
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
        return new EqualsBuilder().append(path, rhs.path).append(agentClusters, rhs.agentClusters).append(processClasses, rhs.processClasses).append(jobs, rhs.jobs).append(schedules, rhs.schedules).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(deliveryDate, rhs.deliveryDate).append(locks, rhs.locks).append(monitors, rhs.monitors).isEquals();
    }

}
