
package com.sos.joc.model.joe.common;

import java.util.Date;
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
 * Deployable record
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deployable")
@JsonPropertyOrder({
    "jobschedulerId",
    "folder",
    "objectName",
    "account",
    "operation",
    "objectType",
    "modified"
})
public class Deployable {

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
    @JsonProperty("objectName")
    @JacksonXmlProperty(localName = "object_name", isAttribute = true)
    private String objectName;
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    private String account;
    @JsonProperty("operation")
    @JacksonXmlProperty(localName = "operation", isAttribute = true)
    private String operation;
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("modified")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    @JacksonXmlProperty(localName = "modified", isAttribute = true)
    private Date modified;

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

    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("operation")
    @JacksonXmlProperty(localName = "operation", isAttribute = true)
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    @JacksonXmlProperty(localName = "operation", isAttribute = true)
    public void setOperation(String operation) {
        this.operation = operation;
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

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("modified")
    @JacksonXmlProperty(localName = "modified", isAttribute = true)
    public Date getModified() {
        return modified;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("modified")
    @JacksonXmlProperty(localName = "modified", isAttribute = true)
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("folder", folder).append("objectName", objectName).append("account", account).append("operation", operation).append("objectType", objectType).append("modified", modified).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folder).append(objectName).append(jobschedulerId).append(objectType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Deployable) == false) {
            return false;
        }
        Deployable rhs = ((Deployable) other);
        return new EqualsBuilder().append(folder, rhs.folder).append(objectName, rhs.objectName).append(jobschedulerId, rhs.jobschedulerId).append(objectType, rhs.objectType).isEquals();
    }

}
