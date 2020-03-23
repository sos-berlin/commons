
package com.sos.joc.model.jobstreams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobStreamJob
 * <p>
 * A job started by the jobstream starter
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobId",
    "job",
    "startDelay"
})
public class JobStreamJob {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobId")
    private Long jobId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    private String job;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("startDelay")
    private Long startDelay;

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobId")
    public Long getJobId() {
        return jobId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobId")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("startDelay")
    public Long getStartDelay() {
        return startDelay;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("startDelay")
    public void setStartDelay(Long startDelay) {
        this.startDelay = startDelay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobId", jobId).append("job", job).append("startDelay", startDelay).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobId).append(startDelay).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamJob) == false) {
            return false;
        }
        JobStreamJob rhs = ((JobStreamJob) other);
        return new EqualsBuilder().append(jobId, rhs.jobId).append(startDelay, rhs.startDelay).append(job, rhs.job).isEquals();
    }

}
