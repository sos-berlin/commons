
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
 * jobStreamFolders
 * <p>
 * List of all jobStreams and their folders
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deliveryDate",
    "jobschedulerId",
    "jobStreamFilter",
    "jobStreamFolders"
})
public class JobStreams {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    private Date deliveryDate;
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobStreamFilter")
    private String jobStreamFilter;
    @JsonProperty("jobStreamFolders")
    private List<Folders2Jobstream> jobStreamFolders = new ArrayList<Folders2Jobstream>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
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
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    @JsonProperty("jobStreamFilter")
    public String getJobStreamFilter() {
        return jobStreamFilter;
    }

    @JsonProperty("jobStreamFilter")
    public void setJobStreamFilter(String jobStreamFilter) {
        this.jobStreamFilter = jobStreamFilter;
    }

    @JsonProperty("jobStreamFolders")
    public List<Folders2Jobstream> getJobStreamFolders() {
        return jobStreamFolders;
    }

    @JsonProperty("jobStreamFolders")
    public void setJobStreamFolders(List<Folders2Jobstream> jobStreamFolders) {
        this.jobStreamFolders = jobStreamFolders;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("jobschedulerId", jobschedulerId).append("jobStreamFilter", jobStreamFilter).append("jobStreamFolders", jobStreamFolders).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStreamFolders).append(deliveryDate).append(jobschedulerId).append(jobStreamFilter).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreams) == false) {
            return false;
        }
        JobStreams rhs = ((JobStreams) other);
        return new EqualsBuilder().append(jobStreamFolders, rhs.jobStreamFolders).append(deliveryDate, rhs.deliveryDate).append(jobschedulerId, rhs.jobschedulerId).append(jobStreamFilter, rhs.jobStreamFilter).isEquals();
    }

}
