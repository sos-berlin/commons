
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
 * jobStreamJob
 * <p>
 * A job started by the jobstream starter
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobId",
    "job",
    "startDelay",
    "nextPeriod",
    "skipOutCondition",
    "jobsInconditions",
    "jobsOutconditions"
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
     * date time
     * <p>
     * Date time. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("nextPeriod")
    @JsonPropertyDescription("Date time. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    private Date nextPeriod;
    @JsonProperty("skipOutCondition")
    private Boolean skipOutCondition;
    @JsonProperty("jobsInconditions")
    private List<JobInCondition> jobsInconditions = new ArrayList<JobInCondition>();
    @JsonProperty("jobsOutconditions")
    private List<JobOutCondition> jobsOutconditions = new ArrayList<JobOutCondition>();

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

    /**
     * date time
     * <p>
     * Date time. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("nextPeriod")
    public Date getNextPeriod() {
        return nextPeriod;
    }

    /**
     * date time
     * <p>
     * Date time. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("nextPeriod")
    public void setNextPeriod(Date nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    @JsonProperty("skipOutCondition")
    public Boolean getSkipOutCondition() {
        return skipOutCondition;
    }

    @JsonProperty("skipOutCondition")
    public void setSkipOutCondition(Boolean skipOutCondition) {
        this.skipOutCondition = skipOutCondition;
    }

    @JsonProperty("jobsInconditions")
    public List<JobInCondition> getJobsInconditions() {
        return jobsInconditions;
    }

    @JsonProperty("jobsInconditions")
    public void setJobsInconditions(List<JobInCondition> jobsInconditions) {
        this.jobsInconditions = jobsInconditions;
    }

    @JsonProperty("jobsOutconditions")
    public List<JobOutCondition> getJobsOutconditions() {
        return jobsOutconditions;
    }

    @JsonProperty("jobsOutconditions")
    public void setJobsOutconditions(List<JobOutCondition> jobsOutconditions) {
        this.jobsOutconditions = jobsOutconditions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobId", jobId).append("job", job).append("startDelay", startDelay).append("nextPeriod", nextPeriod).append("skipOutCondition", skipOutCondition).append("jobsInconditions", jobsInconditions).append("jobsOutconditions", jobsOutconditions).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobId).append(nextPeriod).append(startDelay).append(jobsOutconditions).append(job).append(skipOutCondition).append(jobsInconditions).toHashCode();
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
        return new EqualsBuilder().append(jobId, rhs.jobId).append(nextPeriod, rhs.nextPeriod).append(startDelay, rhs.startDelay).append(jobsOutconditions, rhs.jobsOutconditions).append(job, rhs.job).append(skipOutCondition, rhs.skipOutCondition).append(jobsInconditions, rhs.jobsInconditions).isEquals();
    }

}
