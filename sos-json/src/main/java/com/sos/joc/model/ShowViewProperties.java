
package com.sos.joc.model;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "dashboard",
    "dailyPlan",
    "jobChains",
    "orders",
    "jobs",
    "fileTransfers",
    "resources",
    "history",
    "auditLog",
    "conditions"
})
public class ShowViewProperties {

    @JsonProperty("dashboard")
    private Boolean dashboard;
    @JsonProperty("dailyPlan")
    private Boolean dailyPlan;
    @JsonProperty("jobChains")
    private Boolean jobChains;
    @JsonProperty("orders")
    private Boolean orders;
    @JsonProperty("jobs")
    private Boolean jobs;
    @JsonProperty("fileTransfers")
    private Boolean fileTransfers;
    @JsonProperty("resources")
    private Boolean resources;
    @JsonProperty("history")
    private Boolean history;
    @JsonProperty("auditLog")
    private Boolean auditLog;
    @JsonProperty("conditions")
    private Boolean conditions;

    /**
     * 
     * @return
     *     The dashboard
     */
    @JsonProperty("dashboard")
    public Boolean getDashboard() {
        return dashboard;
    }

    /**
     * 
     * @param dashboard
     *     The dashboard
     */
    @JsonProperty("dashboard")
    public void setDashboard(Boolean dashboard) {
        this.dashboard = dashboard;
    }

    /**
     * 
     * @return
     *     The dailyPlan
     */
    @JsonProperty("dailyPlan")
    public Boolean getDailyPlan() {
        return dailyPlan;
    }

    /**
     * 
     * @param dailyPlan
     *     The dailyPlan
     */
    @JsonProperty("dailyPlan")
    public void setDailyPlan(Boolean dailyPlan) {
        this.dailyPlan = dailyPlan;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public Boolean getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(Boolean jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The orders
     */
    @JsonProperty("orders")
    public Boolean getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    public void setOrders(Boolean orders) {
        this.orders = orders;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public Boolean getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(Boolean jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The fileTransfers
     */
    @JsonProperty("fileTransfers")
    public Boolean getFileTransfers() {
        return fileTransfers;
    }

    /**
     * 
     * @param fileTransfers
     *     The fileTransfers
     */
    @JsonProperty("fileTransfers")
    public void setFileTransfers(Boolean fileTransfers) {
        this.fileTransfers = fileTransfers;
    }

    /**
     * 
     * @return
     *     The resources
     */
    @JsonProperty("resources")
    public Boolean getResources() {
        return resources;
    }

    /**
     * 
     * @param resources
     *     The resources
     */
    @JsonProperty("resources")
    public void setResources(Boolean resources) {
        this.resources = resources;
    }

    /**
     * 
     * @return
     *     The history
     */
    @JsonProperty("history")
    public Boolean getHistory() {
        return history;
    }

    /**
     * 
     * @param history
     *     The history
     */
    @JsonProperty("history")
    public void setHistory(Boolean history) {
        this.history = history;
    }

    /**
     * 
     * @return
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public Boolean getAuditLog() {
        return auditLog;
    }

    /**
     * 
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public void setAuditLog(Boolean auditLog) {
        this.auditLog = auditLog;
    }

    /**
     * 
     * @return
     *     The conditions
     */
    @JsonProperty("conditions")
    public Boolean getConditions() {
        return conditions;
    }

    /**
     * 
     * @param conditions
     *     The conditions
     */
    @JsonProperty("conditions")
    public void setConditions(Boolean conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(dashboard).append(dailyPlan).append(jobChains).append(orders).append(jobs).append(fileTransfers).append(resources).append(history).append(auditLog).append(conditions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ShowViewProperties) == false) {
            return false;
        }
        ShowViewProperties rhs = ((ShowViewProperties) other);
        return new EqualsBuilder().append(dashboard, rhs.dashboard).append(dailyPlan, rhs.dailyPlan).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(jobs, rhs.jobs).append(fileTransfers, rhs.fileTransfers).append(resources, rhs.resources).append(history, rhs.history).append(auditLog, rhs.auditLog).append(conditions, rhs.conditions).isEquals();
    }

}
