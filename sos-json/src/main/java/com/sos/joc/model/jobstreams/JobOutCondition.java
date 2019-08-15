
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job Out-Condition
 * <p>
 * job Out Condition
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "job",
    "outconditions"
})
public class JobOutCondition {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    private String job;
    @JsonProperty("outconditions")
    private List<OutCondition> outconditions = new ArrayList<OutCondition>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The outconditions
     */
    @JsonProperty("outconditions")
    public List<OutCondition> getOutconditions() {
        return outconditions;
    }

    /**
     * 
     * @param outconditions
     *     The outconditions
     */
    @JsonProperty("outconditions")
    public void setOutconditions(List<OutCondition> outconditions) {
        this.outconditions = outconditions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(outconditions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobOutCondition) == false) {
            return false;
        }
        JobOutCondition rhs = ((JobOutCondition) other);
        return new EqualsBuilder().append(job, rhs.job).append(outconditions, rhs.outconditions).isEquals();
    }

}
