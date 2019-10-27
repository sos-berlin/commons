
package com.sos.joc.model.joe.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Answer for Deploy
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deploy_answer")
@JsonPropertyOrder({
    "jobschedulerId",
    "folder",
    "recursive",
    "objectName",
    "objectType",
    "messages"
})
public class DeployAnswer {

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
     * 
     */
    @JsonProperty("folder")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    private String folder;
    @JsonProperty("recursive")
    @JacksonXmlProperty(localName = "recursive", isAttribute = true)
    private Boolean recursive;
    @JsonProperty("objectName")
    @JacksonXmlProperty(localName = "object_name", isAttribute = true)
    private String objectName;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    private JobSchedulerObjectType objectType;
    @JsonProperty("messages")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "messages", isAttribute = false)
    private List<DeployMessage> messages = null;

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
     * 
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public String getFolder() {
        return folder;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public void setFolder(String folder) {
        this.folder = folder;
    }

    @JsonProperty("recursive")
    @JacksonXmlProperty(localName = "recursive", isAttribute = true)
    public Boolean getRecursive() {
        return recursive;
    }

    @JsonProperty("recursive")
    @JacksonXmlProperty(localName = "recursive", isAttribute = true)
    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    @JsonProperty("objectName")
    @JacksonXmlProperty(localName = "object_name", isAttribute = true)
    public String getObjectName() {
        return objectName;
    }

    @JsonProperty("objectName")
    @JacksonXmlProperty(localName = "object_name", isAttribute = true)
    public void setObjectName(String objectName) {
        this.objectName = objectName;
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

    @JsonProperty("messages")
    @JacksonXmlProperty(localName = "messages", isAttribute = false)
    public List<DeployMessage> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    @JacksonXmlProperty(localName = "messages", isAttribute = false)
    public void setMessages(List<DeployMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("folder", folder).append("recursive", recursive).append("objectName", objectName).append("objectType", objectType).append("messages", messages).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folder).append(objectName).append(messages).append(jobschedulerId).append(recursive).append(objectType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeployAnswer) == false) {
            return false;
        }
        DeployAnswer rhs = ((DeployAnswer) other);
        return new EqualsBuilder().append(folder, rhs.folder).append(objectName, rhs.objectName).append(messages, rhs.messages).append(jobschedulerId, rhs.jobschedulerId).append(recursive, rhs.recursive).append(objectType, rhs.objectType).isEquals();
    }

}
