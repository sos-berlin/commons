
package com.sos.joc.model.conditions;

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
 * In-Conditions
 * <p>
 * List of all In Conditions
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "jobSchedulerId",
    "jobsInconditions"
})
public class InConditions {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    @JsonProperty("jobSchedulerId")
    private String jobSchedulerId;
    @JsonProperty("jobsInconditions")
    private List<JobInCondition> jobsInconditions = new ArrayList<JobInCondition>();

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
     *     The jobSchedulerId
     */
    @JsonProperty("jobSchedulerId")
    public String getJobSchedulerId() {
        return jobSchedulerId;
    }

    /**
     * 
     * @param jobSchedulerId
     *     The jobSchedulerId
     */
    @JsonProperty("jobSchedulerId")
    public void setJobSchedulerId(String jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    /**
     * 
     * @return
     *     The jobsInconditions
     */
    @JsonProperty("jobsInconditions")
    public List<JobInCondition> getJobsInconditions() {
        return jobsInconditions;
    }

    /**
     * 
     * @param jobsInconditions
     *     The jobsInconditions
     */
    @JsonProperty("jobsInconditions")
    public void setJobsInconditions(List<JobInCondition> jobsInconditions) {
        this.jobsInconditions = jobsInconditions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobSchedulerId).append(jobsInconditions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof InConditions) == false) {
            return false;
        }
        InConditions rhs = ((InConditions) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobSchedulerId, rhs.jobSchedulerId).append(jobsInconditions, rhs.jobsInconditions).isEquals();
    }

}
