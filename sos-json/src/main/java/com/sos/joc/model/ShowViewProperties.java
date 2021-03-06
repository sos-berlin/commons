
package com.sos.joc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dashboard",
    "dailyPlan",
    "jobChains",
    "orders",
    "jobs",
    "jobStreams",
    "fileTransfers",
    "resources",
    "history",
    "auditLog",
    "conditions",
    "configuration"
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
    @JsonProperty("jobStreams")
    private Boolean jobStreams;
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
    @JsonProperty("configuration")
    private Boolean configuration;

    @JsonProperty("dashboard")
    public Boolean getDashboard() {
        return dashboard;
    }

    @JsonProperty("dashboard")
    public void setDashboard(Boolean dashboard) {
        this.dashboard = dashboard;
    }

    @JsonProperty("dailyPlan")
    public Boolean getDailyPlan() {
        return dailyPlan;
    }

    @JsonProperty("dailyPlan")
    public void setDailyPlan(Boolean dailyPlan) {
        this.dailyPlan = dailyPlan;
    }

    @JsonProperty("jobChains")
    public Boolean getJobChains() {
        return jobChains;
    }

    @JsonProperty("jobChains")
    public void setJobChains(Boolean jobChains) {
        this.jobChains = jobChains;
    }

    @JsonProperty("orders")
    public Boolean getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    public void setOrders(Boolean orders) {
        this.orders = orders;
    }

    @JsonProperty("jobs")
    public Boolean getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(Boolean jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("jobStreams")
    public Boolean getJobStreams() {
        return jobStreams;
    }

    @JsonProperty("jobStreams")
    public void setJobStreams(Boolean jobStreams) {
        this.jobStreams = jobStreams;
    }

    @JsonProperty("fileTransfers")
    public Boolean getFileTransfers() {
        return fileTransfers;
    }

    @JsonProperty("fileTransfers")
    public void setFileTransfers(Boolean fileTransfers) {
        this.fileTransfers = fileTransfers;
    }

    @JsonProperty("resources")
    public Boolean getResources() {
        return resources;
    }

    @JsonProperty("resources")
    public void setResources(Boolean resources) {
        this.resources = resources;
    }

    @JsonProperty("history")
    public Boolean getHistory() {
        return history;
    }

    @JsonProperty("history")
    public void setHistory(Boolean history) {
        this.history = history;
    }

    @JsonProperty("auditLog")
    public Boolean getAuditLog() {
        return auditLog;
    }

    @JsonProperty("auditLog")
    public void setAuditLog(Boolean auditLog) {
        this.auditLog = auditLog;
    }

    @JsonProperty("conditions")
    public Boolean getConditions() {
        return conditions;
    }

    @JsonProperty("conditions")
    public void setConditions(Boolean conditions) {
        this.conditions = conditions;
    }

    @JsonProperty("configuration")
    public Boolean getConfiguration() {
        return configuration;
    }

    @JsonProperty("configuration")
    public void setConfiguration(Boolean configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dashboard", dashboard).append("dailyPlan", dailyPlan).append("jobChains", jobChains).append("orders", orders).append("jobs", jobs).append("jobStreams", jobStreams).append("fileTransfers", fileTransfers).append("resources", resources).append("history", history).append("auditLog", auditLog).append("conditions", conditions).append("configuration", configuration).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStreams).append(auditLog).append(configuration).append(jobs).append(resources).append(history).append(dailyPlan).append(jobChains).append(orders).append(fileTransfers).append(conditions).append(dashboard).toHashCode();
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
        return new EqualsBuilder().append(jobStreams, rhs.jobStreams).append(auditLog, rhs.auditLog).append(configuration, rhs.configuration).append(jobs, rhs.jobs).append(resources, rhs.resources).append(history, rhs.history).append(dailyPlan, rhs.dailyPlan).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(fileTransfers, rhs.fileTransfers).append(conditions, rhs.conditions).append(dashboard, rhs.dashboard).isEquals();
    }

}
