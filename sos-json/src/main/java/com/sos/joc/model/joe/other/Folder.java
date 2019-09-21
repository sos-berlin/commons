
package com.sos.joc.model.joe.other;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    "path",
    "folders",
    "jobs",
    "jobChains",
    "orders",
    "processClasses",
    "locks",
    "schedules",
    "monitors",
    "params",
    "nodeParams",
    "holidays",
    "others"
})
public class Folder {

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
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    private List<String> folders = null;
    @JsonProperty("jobs")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    private List<String> jobs = null;
    @JsonProperty("jobChains")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    private List<String> jobChains = null;
    @JsonProperty("orders")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    private List<String> orders = null;
    @JsonProperty("processClasses")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    private List<String> processClasses = null;
    @JsonProperty("locks")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    private List<String> locks = null;
    @JsonProperty("schedules")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    private List<String> schedules = null;
    @JsonProperty("monitors")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    private List<String> monitors = null;
    /**
     * external parameter files for jobs and orders
     * 
     */
    @JsonProperty("params")
    @JsonPropertyDescription("external parameter files for jobs and orders")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private List<String> params = null;
    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JsonPropertyDescription("[jobChainName].config.xml files with node parameters")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    private List<String> nodeParams = null;
    /**
     * external holidays files
     * 
     */
    @JsonProperty("holidays")
    @JsonPropertyDescription("external holidays files")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    private List<String> holidays = null;
    /**
     * external files, e.g. shell scripts to include in job's script
     * 
     */
    @JsonProperty("others")
    @JsonPropertyDescription("external files, e.g. shell scripts to include in job's script")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    private List<String> others = null;

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
    public List<String> getFolders() {
        return folders;
    }

    @JsonProperty("folders")
    @JacksonXmlProperty(localName = "folders", isAttribute = false)
    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public List<String> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    @JacksonXmlProperty(localName = "jobs", isAttribute = false)
    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public List<String> getJobChains() {
        return jobChains;
    }

    @JsonProperty("jobChains")
    @JacksonXmlProperty(localName = "job_chains", isAttribute = false)
    public void setJobChains(List<String> jobChains) {
        this.jobChains = jobChains;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public List<String> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "orders", isAttribute = false)
    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public List<String> getProcessClasses() {
        return processClasses;
    }

    @JsonProperty("processClasses")
    @JacksonXmlProperty(localName = "process_classes", isAttribute = false)
    public void setProcessClasses(List<String> processClasses) {
        this.processClasses = processClasses;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public List<String> getLocks() {
        return locks;
    }

    @JsonProperty("locks")
    @JacksonXmlProperty(localName = "locks", isAttribute = false)
    public void setLocks(List<String> locks) {
        this.locks = locks;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public List<String> getSchedules() {
        return schedules;
    }

    @JsonProperty("schedules")
    @JacksonXmlProperty(localName = "schedules", isAttribute = false)
    public void setSchedules(List<String> schedules) {
        this.schedules = schedules;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public List<String> getMonitors() {
        return monitors;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(List<String> monitors) {
        this.monitors = monitors;
    }

    /**
     * external parameter files for jobs and orders
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public List<String> getParams() {
        return params;
    }

    /**
     * external parameter files for jobs and orders
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(List<String> params) {
        this.params = params;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public List<String> getNodeParams() {
        return nodeParams;
    }

    /**
     * [jobChainName].config.xml files with node parameters
     * 
     */
    @JsonProperty("nodeParams")
    @JacksonXmlProperty(localName = "node_params", isAttribute = false)
    public void setNodeParams(List<String> nodeParams) {
        this.nodeParams = nodeParams;
    }

    /**
     * external holidays files
     * 
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public List<String> getHolidays() {
        return holidays;
    }

    /**
     * external holidays files
     * 
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public void setHolidays(List<String> holidays) {
        this.holidays = holidays;
    }

    /**
     * external files, e.g. shell scripts to include in job's script
     * 
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public List<String> getOthers() {
        return others;
    }

    /**
     * external files, e.g. shell scripts to include in job's script
     * 
     */
    @JsonProperty("others")
    @JacksonXmlProperty(localName = "others", isAttribute = false)
    public void setOthers(List<String> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("path", path).append("folders", folders).append("jobs", jobs).append("jobChains", jobChains).append("orders", orders).append("processClasses", processClasses).append("locks", locks).append("schedules", schedules).append("monitors", monitors).append("params", params).append("nodeParams", nodeParams).append("holidays", holidays).append("others", others).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folders).append(processClasses).append(jobs).append(params).append(locks).append(path).append(holidays).append(schedules).append(jobChains).append(orders).append(nodeParams).append(others).append(monitors).toHashCode();
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
        return new EqualsBuilder().append(folders, rhs.folders).append(processClasses, rhs.processClasses).append(jobs, rhs.jobs).append(params, rhs.params).append(locks, rhs.locks).append(path, rhs.path).append(holidays, rhs.holidays).append(schedules, rhs.schedules).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(nodeParams, rhs.nodeParams).append(others, rhs.others).append(monitors, rhs.monitors).isEquals();
    }

}
