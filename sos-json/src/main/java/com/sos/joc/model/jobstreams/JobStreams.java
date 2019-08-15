
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
     * @return
     *     The deliveryDate
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
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The jobStreamFilter
     */
    @JsonProperty("jobStreamFilter")
    public String getJobStreamFilter() {
        return jobStreamFilter;
    }

    /**
     * 
     * @param jobStreamFilter
     *     The jobStreamFilter
     */
    @JsonProperty("jobStreamFilter")
    public void setJobStreamFilter(String jobStreamFilter) {
        this.jobStreamFilter = jobStreamFilter;
    }

    /**
     * 
     * @return
     *     The jobStreamFolders
     */
    @JsonProperty("jobStreamFolders")
    public List<Folders2Jobstream> getJobStreamFolders() {
        return jobStreamFolders;
    }

    /**
     * 
     * @param jobStreamFolders
     *     The jobStreamFolders
     */
    @JsonProperty("jobStreamFolders")
    public void setJobStreamFolders(List<Folders2Jobstream> jobStreamFolders) {
        this.jobStreamFolders = jobStreamFolders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobschedulerId).append(jobStreamFilter).append(jobStreamFolders).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobschedulerId, rhs.jobschedulerId).append(jobStreamFilter, rhs.jobStreamFilter).append(jobStreamFolders, rhs.jobStreamFolders).isEquals();
    }

}
