
package com.sos.joc.model.joe.other;

import java.util.Date;
import java.util.Set;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "path",
    "_message",
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
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    @JsonProperty("_message")
    @JacksonXmlProperty(localName = "_message", isAttribute = true)
    private String _message;
    @JsonProperty("folders")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    private Set<FolderItem> folders = null;
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
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    private Set<FolderItem> nodeParams = null;
    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     */
    @JsonProperty("others")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    private Set<FolderItem> others = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Folder() {
    }

    /**
     * 
     * @param folders
     * @param processClasses
     * @param jobs
     * @param locks
     * @param path
     * @param _message
     * @param schedules
     * @param jobChains
     * @param orders
     * @param deliveryDate
     * @param nodeParams
     * @param others
     * @param monitors
     */
    public Folder(Date deliveryDate, String path, String _message, Set<FolderItem> folders, Set<FolderItem> jobs, Set<FolderItem> jobChains, Set<FolderItem> orders, Set<FolderItem> processClasses, Set<FolderItem> locks, Set<FolderItem> schedules, Set<FolderItem> monitors, Set<FolderItem> nodeParams, Set<FolderItem> others) {
        this.deliveryDate = deliveryDate;
        this.path = path;
        this._message = _message;
        this.folders = folders;
        this.jobs = jobs;
        this.jobChains = jobChains;
        this.orders = orders;
        this.processClasses = processClasses;
        this.locks = locks;
        this.schedules = schedules;
        this.monitors = monitors;
        this.nodeParams = nodeParams;
        this.others = others;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
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
     * @param deliveryDate
     *     The deliveryDate
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
     * @return
     *     The path
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
     * @param path
     *     The path
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The _message
     */
    @JsonProperty("_message")
    @JacksonXmlProperty(localName = "_message", isAttribute = true)
    public String get_message() {
        return _message;
    }

    /**
     * 
     * @param _message
     *     The _message
     */
    @JsonProperty("_message")
    @JacksonXmlProperty(localName = "_message", isAttribute = true)
    public void set_message(String _message) {
        this._message = _message;
    }

    /**
     * 
     * @return
     *     The folders
     */
    @JsonProperty("folders")
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    public Set<FolderItem> getFolders() {
        return folders;
    }

    /**
     * 
     * @param folders
     *     The folders
     */
    @JsonProperty("folders")
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    public void setFolders(Set<FolderItem> folders) {
        this.folders = folders;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public Set<FolderItem> getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public void setJobs(Set<FolderItem> jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public Set<FolderItem> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public void setJobChains(Set<FolderItem> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The orders
     */
    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public Set<FolderItem> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public void setOrders(Set<FolderItem> orders) {
        this.orders = orders;
    }

    /**
     * 
     * @return
     *     The processClasses
     */
    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public Set<FolderItem> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * @param processClasses
     *     The processClasses
     */
    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public void setProcessClasses(Set<FolderItem> processClasses) {
        this.processClasses = processClasses;
    }

    /**
     * 
     * @return
     *     The locks
     */
    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public Set<FolderItem> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public void setLocks(Set<FolderItem> locks) {
        this.locks = locks;
    }

    /**
     * 
     * @return
     *     The schedules
     */
    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public Set<FolderItem> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public void setSchedules(Set<FolderItem> schedules) {
        this.schedules = schedules;
    }

    /**
     * 
     * @return
     *     The monitors
     */
    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public Set<FolderItem> getMonitors() {
        return monitors;
    }

    /**
     * 
     * @param monitors
     *     The monitors
     */
    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(Set<FolderItem> monitors) {
        this.monitors = monitors;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     * @return
     *     The nodeParams
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public Set<FolderItem> getNodeParams() {
        return nodeParams;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     * @param nodeParams
     *     The nodeParams
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public void setNodeParams(Set<FolderItem> nodeParams) {
        this.nodeParams = nodeParams;
    }

    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     * @return
     *     The others
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public Set<FolderItem> getOthers() {
        return others;
    }

    /**
     * external files, e.g. shell scripts to include in job's script, parameter files for jobs and orders, holidays files
     * 
     * @param others
     *     The others
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public void setOthers(Set<FolderItem> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(path).append(_message).append(folders).append(jobs).append(jobChains).append(orders).append(processClasses).append(locks).append(schedules).append(monitors).append(nodeParams).append(others).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(path, rhs.path).append(_message, rhs._message).append(folders, rhs.folders).append(jobs, rhs.jobs).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(processClasses, rhs.processClasses).append(locks, rhs.locks).append(schedules, rhs.schedules).append(monitors, rhs.monitors).append(nodeParams, rhs.nodeParams).append(others, rhs.others).isEquals();
    }

}
