
package com.sos.joc.model.jobstreams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * conditionEvent
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "event",
    "globalEvent",
    "path",
    "jobStream",
    "session",
    "outConditionId"
})
public class ConditionEvent {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("event")
    private String event;
    @JsonProperty("globalEvent")
    private Boolean globalEvent;
    @JsonProperty("path")
    private String path;
    @JsonProperty("jobStream")
    private String jobStream;
    @JsonProperty("session")
    private String session;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("outConditionId")
    private Long outConditionId;

    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    @JsonProperty("event")
    public String getEvent() {
        return event;
    }

    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    @JsonProperty("globalEvent")
    public Boolean getGlobalEvent() {
        return globalEvent;
    }

    @JsonProperty("globalEvent")
    public void setGlobalEvent(Boolean globalEvent) {
        this.globalEvent = globalEvent;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    @JsonProperty("session")
    public String getSession() {
        return session;
    }

    @JsonProperty("session")
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("outConditionId")
    public Long getOutConditionId() {
        return outConditionId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("outConditionId")
    public void setOutConditionId(Long outConditionId) {
        this.outConditionId = outConditionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("event", event).append("globalEvent", globalEvent).append("path", path).append("jobStream", jobStream).append("session", session).append("outConditionId", outConditionId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(session).append(jobStream).append(jobschedulerId).append(event).append(globalEvent).append(outConditionId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConditionEvent) == false) {
            return false;
        }
        ConditionEvent rhs = ((ConditionEvent) other);
        return new EqualsBuilder().append(path, rhs.path).append(session, rhs.session).append(jobStream, rhs.jobStream).append(jobschedulerId, rhs.jobschedulerId).append(event, rhs.event).append(globalEvent, rhs.globalEvent).append(outConditionId, rhs.outConditionId).isEquals();
    }

}
