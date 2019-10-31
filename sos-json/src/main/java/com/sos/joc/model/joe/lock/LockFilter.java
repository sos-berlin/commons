
package com.sos.joc.model.joe.lock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lock filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "lock_filter")
@JsonPropertyOrder({
    "jobschedulerId",
    "folder",
    "forceLock"
})
public class LockFilter {

    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
    /**
     * path of the a folder
     * 
     */
    @JsonProperty("folder")
    @JsonPropertyDescription("path of the a folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    private String folder;
    @JsonProperty("forceLock")
    @JacksonXmlProperty(localName = "force_lock", isAttribute = true)
    private Boolean forceLock = false;

    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path of the a folder
     * 
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public String getFolder() {
        return folder;
    }

    /**
     * path of the a folder
     * 
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public void setFolder(String folder) {
        this.folder = folder;
    }

    @JsonProperty("forceLock")
    @JacksonXmlProperty(localName = "force_lock", isAttribute = true)
    public Boolean getForceLock() {
        return forceLock;
    }

    @JsonProperty("forceLock")
    @JacksonXmlProperty(localName = "force_lock", isAttribute = true)
    public void setForceLock(Boolean forceLock) {
        this.forceLock = forceLock;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("folder", folder).append("forceLock", forceLock).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folder).append(jobschedulerId).append(forceLock).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockFilter) == false) {
            return false;
        }
        LockFilter rhs = ((LockFilter) other);
        return new EqualsBuilder().append(folder, rhs.folder).append(jobschedulerId, rhs.jobschedulerId).append(forceLock, rhs.forceLock).isEquals();
    }

}
