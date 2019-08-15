
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
 * JobStream Conditions
 * <p>
 * List of all Conditions using an event
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobStream",
    "jobs"
})
public class JobstreamConditions {

    @JsonProperty("jobStream")
    private String jobStream;
    @JsonProperty("jobs")
    private List<ConditionRef> jobs = new ArrayList<ConditionRef>();

    /**
     * 
     * @return
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * 
     * @param jobStream
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public List<ConditionRef> getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(List<ConditionRef> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStream).append(jobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobstreamConditions) == false) {
            return false;
        }
        JobstreamConditions rhs = ((JobstreamConditions) other);
        return new EqualsBuilder().append(jobStream, rhs.jobStream).append(jobs, rhs.jobs).isEquals();
    }

}
