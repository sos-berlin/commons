
package com.sos.joc.model.joe.common;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "deploy_answer")
@Generated("org.jsonschema2pojo")
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
     * No args constructor for use in serialization
     * 
     */
    public DeployAnswer() {
    }

    /**
     * 
     * @param folder
     * @param objectName
     * @param messages
     * @param jobschedulerId
     * @param recursive
     * @param objectType
     */
    public DeployAnswer(String jobschedulerId, String folder, Boolean recursive, String objectName, JobSchedulerObjectType objectType, List<DeployMessage> messages) {
        this.jobschedulerId = jobschedulerId;
        this.folder = folder;
        this.recursive = recursive;
        this.objectName = objectName;
        this.objectType = objectType;
        this.messages = messages;
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
     * 
     * @return
     *     The folder
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
     * @param folder
     *     The folder
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * 
     * @return
     *     The recursive
     */
    @JsonProperty("recursive")
    @JacksonXmlProperty(localName = "recursive", isAttribute = true)
    public Boolean getRecursive() {
        return recursive;
    }

    /**
     * 
     * @param recursive
     *     The recursive
     */
    @JsonProperty("recursive")
    @JacksonXmlProperty(localName = "recursive", isAttribute = true)
    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    /**
     * 
     * @return
     *     The objectName
     */
    @JsonProperty("objectName")
    @JacksonXmlProperty(localName = "object_name", isAttribute = true)
    public String getObjectName() {
        return objectName;
    }

    /**
     * 
     * @param objectName
     *     The objectName
     */
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
     *     The messages
     */
    @JsonProperty("messages")
    @JacksonXmlProperty(localName = "messages", isAttribute = false)
    public List<DeployMessage> getMessages() {
        return messages;
    }

    /**
     * 
     * @param messages
     *     The messages
     */
    @JsonProperty("messages")
    @JacksonXmlProperty(localName = "messages", isAttribute = false)
    public void setMessages(List<DeployMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(folder).append(recursive).append(objectName).append(objectType).append(messages).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(folder, rhs.folder).append(recursive, rhs.recursive).append(objectName, rhs.objectName).append(objectType, rhs.objectType).append(messages, rhs.messages).isEquals();
    }

}
