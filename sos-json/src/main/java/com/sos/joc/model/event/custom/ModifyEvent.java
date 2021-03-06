
package com.sos.joc.model.event.custom;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.NameValuePair;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify custom event
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eventjobChain",
    "orderId",
    "jobChain",
    "job",
    "jobschedulerId",
    "eventClass",
    "eventId",
    "exitCode",
    "expiresTimezone",
    "expires",
    "expirationPeriod",
    "expirationCycle",
    "params",
    "auditLog"
})
public class ModifyEvent {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("eventjobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String eventjobChain;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String jobChain;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String job;
    /**
     * filename
     * <p>
     * 
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventClass")
    private String eventClass;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventId")
    private String eventId;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    private Integer exitCode;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expiresTimezone")
    private String expiresTimezone;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expires")
    private String expires;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationPeriod")
    private String expirationPeriod;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationCycle")
    private String expirationCycle;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("eventjobChain")
    public String getEventjobChain() {
        return eventjobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("eventjobChain")
    public void setEventjobChain(String eventjobChain) {
        this.eventjobChain = eventjobChain;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * filename
     * <p>
     * 
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * filename
     * <p>
     * 
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventClass")
    public String getEventClass() {
        return eventClass;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventClass")
    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expiresTimezone")
    public String getExpiresTimezone() {
        return expiresTimezone;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expiresTimezone")
    public void setExpiresTimezone(String expiresTimezone) {
        this.expiresTimezone = expiresTimezone;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expires")
    public String getExpires() {
        return expires;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expires")
    public void setExpires(String expires) {
        this.expires = expires;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationPeriod")
    public String getExpirationPeriod() {
        return expirationPeriod;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationPeriod")
    public void setExpirationPeriod(String expirationPeriod) {
        this.expirationPeriod = expirationPeriod;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationCycle")
    public String getExpirationCycle() {
        return expirationCycle;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("expirationCycle")
    public void setExpirationCycle(String expirationCycle) {
        this.expirationCycle = expirationCycle;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    public List<NameValuePair> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    public void setParams(List<NameValuePair> params) {
        this.params = params;
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
        return new ToStringBuilder(this).append("eventjobChain", eventjobChain).append("orderId", orderId).append("jobChain", jobChain).append("job", job).append("jobschedulerId", jobschedulerId).append("eventClass", eventClass).append("eventId", eventId).append("exitCode", exitCode).append("expiresTimezone", expiresTimezone).append("expires", expires).append("expirationPeriod", expirationPeriod).append("expirationCycle", expirationCycle).append("params", params).append("auditLog", auditLog).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventId).append(expires).append(eventClass).append(auditLog).append(orderId).append(expirationCycle).append(jobChain).append(params).append(expirationPeriod).append(expiresTimezone).append(eventjobChain).append(exitCode).append(job).append(jobschedulerId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyEvent) == false) {
            return false;
        }
        ModifyEvent rhs = ((ModifyEvent) other);
        return new EqualsBuilder().append(eventId, rhs.eventId).append(expires, rhs.expires).append(eventClass, rhs.eventClass).append(auditLog, rhs.auditLog).append(orderId, rhs.orderId).append(expirationCycle, rhs.expirationCycle).append(jobChain, rhs.jobChain).append(params, rhs.params).append(expirationPeriod, rhs.expirationPeriod).append(expiresTimezone, rhs.expiresTimezone).append(eventjobChain, rhs.eventjobChain).append(exitCode, rhs.exitCode).append(job, rhs.job).append(jobschedulerId, rhs.jobschedulerId).isEquals();
    }

}
