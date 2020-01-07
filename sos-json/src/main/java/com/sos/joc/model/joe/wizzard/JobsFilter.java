
package com.sos.joc.model.joe.wizzard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobs filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "jobs_filter")
@JsonPropertyOrder({
    "jobschedulerId",
    "path",
    "isOrderJob"
})
public class JobsFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    private Boolean isOrderJob;

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public String getPath() {
        return path;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("path", path).append("isOrderJob", isOrderJob).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(isOrderJob).append(path).append(jobschedulerId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsFilter) == false) {
            return false;
        }
        JobsFilter rhs = ((JobsFilter) other);
        return new EqualsBuilder().append(isOrderJob, rhs.isOrderJob).append(path, rhs.path).append(jobschedulerId, rhs.jobschedulerId).isEquals();
    }

}
