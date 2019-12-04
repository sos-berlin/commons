
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
 * Deploy message
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deploy_message")
@JsonPropertyOrder({
    "action",
    "path",
    "objectType",
    "failReason"
})
public class DeployMessage {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("action")
    @JacksonXmlProperty(localName = "action", isAttribute = false)
    private DeployActionType action;
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
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    private JobSchedulerObjectType objectType;
    @JsonProperty("failReason")
    @JacksonXmlProperty(localName = "fail_reason", isAttribute = false)
    private DeployFailReason failReason;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("action")
    @JacksonXmlProperty(localName = "action", isAttribute = false)
    public DeployActionType getAction() {
        return action;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("action")
    @JacksonXmlProperty(localName = "action", isAttribute = false)
    public void setAction(DeployActionType action) {
        this.action = action;
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
     * JobScheduler object type
     * <p>
     * 
     * (Required)
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
     * (Required)
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    @JsonProperty("failReason")
    @JacksonXmlProperty(localName = "fail_reason", isAttribute = false)
    public DeployFailReason getFailReason() {
        return failReason;
    }

    @JsonProperty("failReason")
    @JacksonXmlProperty(localName = "fail_reason", isAttribute = false)
    public void setFailReason(DeployFailReason failReason) {
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("action", action).append("path", path).append("objectType", objectType).append("failReason", failReason).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(action).append(path).append(failReason).append(objectType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeployMessage) == false) {
            return false;
        }
        DeployMessage rhs = ((DeployMessage) other);
        return new EqualsBuilder().append(action, rhs.action).append(path, rhs.path).append(failReason, rhs.failReason).append(objectType, rhs.objectType).isEquals();
    }

}
