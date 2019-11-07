
package com.sos.joc.model.tree;

import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sos.joc.model.joe.other.FolderItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * joe folder
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobs",
    "jobChains",
    "orders",
    "agentClusters",
    "processClasses",
    "locks",
    "schedules",
    "monitors",
    "nodeParams"
})
public class JoeTree
    extends Tree
{

    @JsonProperty("jobs")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> jobs = new LinkedHashSet<FolderItem>();
    @JsonProperty("jobChains")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> jobChains = new LinkedHashSet<FolderItem>();
    @JsonProperty("orders")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> orders = new LinkedHashSet<FolderItem>();
    @JsonProperty("agentClusters")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> agentClusters = new LinkedHashSet<FolderItem>();
    @JsonProperty("processClasses")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> processClasses = new LinkedHashSet<FolderItem>();
    @JsonProperty("locks")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> locks = new LinkedHashSet<FolderItem>();
    @JsonProperty("schedules")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> schedules = new LinkedHashSet<FolderItem>();
    @JsonProperty("monitors")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> monitors = new LinkedHashSet<FolderItem>();
    @JsonProperty("nodeParams")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<FolderItem> nodeParams = new LinkedHashSet<FolderItem>();

    @JsonProperty("jobs")
    public Set<FolderItem> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(Set<FolderItem> jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("jobChains")
    public Set<FolderItem> getJobChains() {
        return jobChains;
    }

    @JsonProperty("jobChains")
    public void setJobChains(Set<FolderItem> jobChains) {
        this.jobChains = jobChains;
    }

    @JsonProperty("orders")
    public Set<FolderItem> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    public void setOrders(Set<FolderItem> orders) {
        this.orders = orders;
    }

    @JsonProperty("agentClusters")
    public Set<FolderItem> getAgentClusters() {
        return agentClusters;
    }

    @JsonProperty("agentClusters")
    public void setAgentClusters(Set<FolderItem> agentClusters) {
        this.agentClusters = agentClusters;
    }

    @JsonProperty("processClasses")
    public Set<FolderItem> getProcessClasses() {
        return processClasses;
    }

    @JsonProperty("processClasses")
    public void setProcessClasses(Set<FolderItem> processClasses) {
        this.processClasses = processClasses;
    }

    @JsonProperty("locks")
    public Set<FolderItem> getLocks() {
        return locks;
    }

    @JsonProperty("locks")
    public void setLocks(Set<FolderItem> locks) {
        this.locks = locks;
    }

    @JsonProperty("schedules")
    public Set<FolderItem> getSchedules() {
        return schedules;
    }

    @JsonProperty("schedules")
    public void setSchedules(Set<FolderItem> schedules) {
        this.schedules = schedules;
    }

    @JsonProperty("monitors")
    public Set<FolderItem> getMonitors() {
        return monitors;
    }

    @JsonProperty("monitors")
    public void setMonitors(Set<FolderItem> monitors) {
        this.monitors = monitors;
    }

    @JsonProperty("nodeParams")
    public Set<FolderItem> getNodeParams() {
        return nodeParams;
    }

    @JsonProperty("nodeParams")
    public void setNodeParams(Set<FolderItem> nodeParams) {
        this.nodeParams = nodeParams;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("jobs", jobs).append("jobChains", jobChains).append("orders", orders).append("agentClusters", agentClusters).append("processClasses", processClasses).append("locks", locks).append("schedules", schedules).append("monitors", monitors).append("nodeParams", nodeParams).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JoeTree) == false) {
            return false;
        }
        JoeTree rhs = ((JoeTree) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).isEquals();
    }

}
