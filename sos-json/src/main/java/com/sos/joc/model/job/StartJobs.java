
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.audit.AuditParams;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * start job commands
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "jobs",
    "auditLog"
})
public class StartJobs {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    private List<StartJob> jobs = new ArrayList<StartJob>();
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    public List<StartJob> getJobs() {
        return jobs;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    public void setJobs(List<StartJob> jobs) {
        this.jobs = jobs;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("jobs", jobs).append("auditLog", auditLog).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(auditLog).append(jobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartJobs) == false) {
            return false;
        }
        StartJobs rhs = ((StartJobs) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(auditLog, rhs.auditLog).append(jobs, rhs.jobs).isEquals();
    }

}
