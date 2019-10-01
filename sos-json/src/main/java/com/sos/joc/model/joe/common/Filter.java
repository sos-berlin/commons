
package com.sos.joc.model.joe.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "filter")
@Generated("org.jsonschema2pojo")
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
     * No args constructor for use in serialization
     * 
     */
    public Filter() {
    }

    /**
     * 
     * @param path
     * @param forceLive
     * @param oldPath
     * @param jobschedulerId
     * @param object
     * @param objectType
     */
    public Filter(String jobschedulerId, String path, String oldPath, Boolean forceLive, String object, JobSchedulerObjectType objectType) {
        this.jobschedulerId = jobschedulerId;
        this.path = path;
        this.oldPath = oldPath;
        this.forceLive = forceLive;
        this.object = object;
        this.objectType = objectType;
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
     * 
     * @return
     *     The object
     */
    @JsonProperty("object")
    @JacksonXmlProperty(localName = "object", isAttribute = true)
    public String getObject() {
        return object;
    }

    /**
     * 
     * @param object
     *     The object
     */
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(path).append(oldPath).append(forceLive).append(object).append(objectType).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(path, rhs.path).append(oldPath, rhs.oldPath).append(forceLive, rhs.forceLive).append(object, rhs.object).append(objectType, rhs.objectType).isEquals();
    }

}
