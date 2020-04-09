
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.joe.schedule.RunTime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobStreamStarter
 * <p>
 * List of all jobStream starters
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "state",
    "jobStreamStarterId",
    "title",
    "nextStart",
    "jobs",
    "runTime",
    "params"
})
public class JobStreamStarter {

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private String state;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamStarterId")
    private Long jobStreamStarterId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("title")
    private String title;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("nextStart")
    private String nextStart;
    @JsonProperty("jobs")
    private List<JobStreamJob> jobs = new ArrayList<JobStreamJob>();
    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    private RunTime runTime;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamStarterId")
    public Long getJobStreamStarterId() {
        return jobStreamStarterId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamStarterId")
    public void setJobStreamStarterId(Long jobStreamStarterId) {
        this.jobStreamStarterId = jobStreamStarterId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("nextStart")
    public String getNextStart() {
        return nextStart;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("nextStart")
    public void setNextStart(String nextStart) {
        this.nextStart = nextStart;
    }

    @JsonProperty("jobs")
    public List<JobStreamJob> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(List<JobStreamJob> jobs) {
        this.jobs = jobs;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    public RunTime getRunTime() {
        return runTime;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    public void setRunTime(RunTime runTime) {
        this.runTime = runTime;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    public List<NameValuePair> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("state", state).append("jobStreamStarterId", jobStreamStarterId).append("title", title).append("nextStart", nextStart).append("jobs", jobs).append("runTime", runTime).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobs).append(jobStreamStarterId).append(state).append(nextStart).append(runTime).append(title).append(params).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamStarter) == false) {
            return false;
        }
        JobStreamStarter rhs = ((JobStreamStarter) other);
        return new EqualsBuilder().append(jobs, rhs.jobs).append(jobStreamStarterId, rhs.jobStreamStarterId).append(state, rhs.state).append(nextStart, rhs.nextStart).append(runTime, rhs.runTime).append(title, rhs.title).append(params, rhs.params).isEquals();
    }

}
