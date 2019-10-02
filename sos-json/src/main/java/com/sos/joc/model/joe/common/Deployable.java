
package com.sos.joc.model.joe.common;

import java.util.Date;
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
 * Deployable record
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "deployable")
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "modified", isAttribute = true)
    private Date modified;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Deployable() {
    }

    /**
     * 
     * @param folder
     * @param objectName
     * @param modified
     * @param jobschedulerId
     * @param operation
     * @param account
     * @param objectType
     */
    public Deployable(String jobschedulerId, String folder, String objectName, String account, String operation, JobSchedulerObjectType objectType, Date modified) {
        this.jobschedulerId = jobschedulerId;
        this.folder = folder;
        this.objectName = objectName;
        this.account = account;
        this.operation = operation;
        this.objectType = objectType;
        this.modified = modified;
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
     * 
     * @return
     *     The account
     */
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public String getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 
     * @return
     *     The operation
     */
    @JsonProperty("operation")
    @JacksonXmlProperty(localName = "operation", isAttribute = true)
    public String getOperation() {
        return operation;
    }

    /**
     * 
     * @param operation
     *     The operation
     */
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The modified
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
     * @param modified
     *     The modified
     */
    @JsonProperty("modified")
    @JacksonXmlProperty(localName = "modified", isAttribute = true)
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(folder).append(objectName).append(account).append(operation).append(objectType).append(modified).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(folder, rhs.folder).append(objectName, rhs.objectName).append(account, rhs.account).append(operation, rhs.operation).append(objectType, rhs.objectType).append(modified, rhs.modified).isEquals();
    }

}
