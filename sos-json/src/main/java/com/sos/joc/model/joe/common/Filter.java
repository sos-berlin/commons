
package com.sos.joc.model.joe.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * filter for requests
 * <p>
 * oldPath is used for a move/rename, auditLog only for deploy
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "filter")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "path",
    "oldPath",
    "objectType",
    "forceLive",
    "auditLog"
})
public class Filter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
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
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    private String oldPath;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    private JobSchedulerObjectType objectType;
    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    private Boolean forceLive;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    private AuditParams auditLog;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Filter() {
    }

    /**
     * 
     * @param path
     * @param auditLog
     * @param forceLive
     * @param oldPath
     * @param jobschedulerId
     * @param objectType
     */
    public Filter(String jobschedulerId, String path, String oldPath, JobSchedulerObjectType objectType, Boolean forceLive, AuditParams auditLog) {
        this.jobschedulerId = jobschedulerId;
        this.path = path;
        this.oldPath = oldPath;
        this.objectType = objectType;
        this.forceLive = forceLive;
        this.auditLog = auditLog;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
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
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The oldPath
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    public String getOldPath() {
        return oldPath;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param oldPath
     *     The oldPath
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    /**
     * 
     * @return
     *     The forceLive
     */
    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    public Boolean getForceLive() {
        return forceLive;
    }

    /**
     * 
     * @param forceLive
     *     The forceLive
     */
    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    public void setForceLive(Boolean forceLive) {
        this.forceLive = forceLive;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @return
     *     The auditLog
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(path).append(oldPath).append(objectType).append(forceLive).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Filter) == false) {
            return false;
        }
        Filter rhs = ((Filter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(path, rhs.path).append(oldPath, rhs.oldPath).append(objectType, rhs.objectType).append(forceLive, rhs.forceLive).append(auditLog, rhs.auditLog).isEquals();
    }

}
