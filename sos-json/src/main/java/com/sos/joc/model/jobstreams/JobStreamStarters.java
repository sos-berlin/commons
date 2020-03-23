
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobStream Starters
 * <p>
 * List of all JobStream Starters
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deliveryDate",
    "jobschedulerId",
    "jobStream",
    "jobstreamStarters"
})
public class JobStreamStarters {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    private Date deliveryDate;
    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    private Long jobStream;
    @JsonProperty("jobstreamStarters")
    private List<JobStreamStarter> jobstreamStarters = new ArrayList<JobStreamStarter>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public Long getJobStream() {
        return jobStream;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public void setJobStream(Long jobStream) {
        this.jobStream = jobStream;
    }

    @JsonProperty("jobstreamStarters")
    public List<JobStreamStarter> getJobstreamStarters() {
        return jobstreamStarters;
    }

    @JsonProperty("jobstreamStarters")
    public void setJobstreamStarters(List<JobStreamStarter> jobstreamStarters) {
        this.jobstreamStarters = jobstreamStarters;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("jobschedulerId", jobschedulerId).append("jobStream", jobStream).append("jobstreamStarters", jobstreamStarters).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStream).append(jobstreamStarters).append(deliveryDate).append(jobschedulerId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamStarters) == false) {
            return false;
        }
        JobStreamStarters rhs = ((JobStreamStarters) other);
        return new EqualsBuilder().append(jobStream, rhs.jobStream).append(jobstreamStarters, rhs.jobstreamStarters).append(deliveryDate, rhs.deliveryDate).append(jobschedulerId, rhs.jobschedulerId).isEquals();
    }

}
