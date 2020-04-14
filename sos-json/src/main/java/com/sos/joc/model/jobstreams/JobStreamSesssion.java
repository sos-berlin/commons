
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
 * JobStreamSession
 * <p>
 * Reset Workflow, unconsume expressions
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "jobStreamId",
    "jobStream",
    "session",
    "running",
    "started",
    "ended",
    "jobstreamTasks"
})
public class JobStreamSesssion {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamId")
    private Long jobStreamId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    private String jobStream;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("session")
    private String session;
    @JsonProperty("running")
    private Boolean running;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("started")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date started;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("ended")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date ended;
    @JsonProperty("jobstreamTasks")
    private List<JobStreamTask> jobstreamTasks = new ArrayList<JobStreamTask>();

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamId")
    public Long getJobStreamId() {
        return jobStreamId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStreamId")
    public void setJobStreamId(Long jobStreamId) {
        this.jobStreamId = jobStreamId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("session")
    public String getSession() {
        return session;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("session")
    public void setSession(String session) {
        this.session = session;
    }

    @JsonProperty("running")
    public Boolean getRunning() {
        return running;
    }

    @JsonProperty("running")
    public void setRunning(Boolean running) {
        this.running = running;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("started")
    public Date getStarted() {
        return started;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("started")
    public void setStarted(Date started) {
        this.started = started;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("ended")
    public Date getEnded() {
        return ended;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("ended")
    public void setEnded(Date ended) {
        this.ended = ended;
    }

    @JsonProperty("jobstreamTasks")
    public List<JobStreamTask> getJobstreamTasks() {
        return jobstreamTasks;
    }

    @JsonProperty("jobstreamTasks")
    public void setJobstreamTasks(List<JobStreamTask> jobstreamTasks) {
        this.jobstreamTasks = jobstreamTasks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("jobStreamId", jobStreamId).append("jobStream", jobStream).append("session", session).append("running", running).append("started", started).append("ended", ended).append("jobstreamTasks", jobstreamTasks).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(running).append(jobstreamTasks).append(session).append(jobStream).append(ended).append(started).append(id).append(jobStreamId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamSesssion) == false) {
            return false;
        }
        JobStreamSesssion rhs = ((JobStreamSesssion) other);
        return new EqualsBuilder().append(running, rhs.running).append(jobstreamTasks, rhs.jobstreamTasks).append(session, rhs.session).append(jobStream, rhs.jobStream).append(ended, rhs.ended).append(started, rhs.started).append(id, rhs.id).append(jobStreamId, rhs.jobStreamId).isEquals();
    }

}