
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.job.JobPath;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * condition Jobs Filter
 * <p>
 * Reset Workflow, unconsume expressions
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "jobs",
    "context"
})
public class ConditionJobsFilter {

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobs")
    private List<JobPath> jobs = new ArrayList<JobPath>();
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("context")
    private String context;

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

    @JsonProperty("jobs")
    public List<JobPath> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(List<JobPath> jobs) {
        this.jobs = jobs;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("context")
    public String getContext() {
        return context;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("context")
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("jobs", jobs).append("context", context).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(context).append(jobschedulerId).append(jobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConditionJobsFilter) == false) {
            return false;
        }
        ConditionJobsFilter rhs = ((ConditionJobsFilter) other);
        return new EqualsBuilder().append(context, rhs.context).append(jobschedulerId, rhs.jobschedulerId).append(jobs, rhs.jobs).isEquals();
    }

}
