
package com.sos.joc.model.joe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * filter for joe requests
 * <p>
 * oldPath is used for a move/rename, auditLog only for deploy, forceLive only for read/file
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "filter")
@JsonPropertyOrder({
    "jobschedulerId",
    "path",
    "oldPath",
    "forceLive",
    "object",
    "objectType"
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
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("oldPath")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    private String oldPath;
    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    private Boolean forceLive;
    @JsonProperty("object")
    @JacksonXmlProperty(localName = "object", isAttribute = true)
    private String object;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    private JobSchedulerObjectType objectType;

    /**
     * 
     * (Required)
     * 
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

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
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
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    public Boolean getForceLive() {
        return forceLive;
    }

    @JsonProperty("forceLive")
    @JacksonXmlProperty(localName = "force_live", isAttribute = true)
    public void setForceLive(Boolean forceLive) {
        this.forceLive = forceLive;
    }

    @JsonProperty("object")
    @JacksonXmlProperty(localName = "object", isAttribute = true)
    public String getObject() {
        return object;
    }

    @JsonProperty("object")
    @JacksonXmlProperty(localName = "object", isAttribute = true)
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
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
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("path", path).append("oldPath", oldPath).append("forceLive", forceLive).append("object", object).append("objectType", objectType).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(forceLive).append(oldPath).append(jobschedulerId).append(object).append(objectType).toHashCode();
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
        return new EqualsBuilder().append(path, rhs.path).append(forceLive, rhs.forceLive).append(oldPath, rhs.oldPath).append(jobschedulerId, rhs.jobschedulerId).append(object, rhs.object).append(objectType, rhs.objectType).isEquals();
    }

}
