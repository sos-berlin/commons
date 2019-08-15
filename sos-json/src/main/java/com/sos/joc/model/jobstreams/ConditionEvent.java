
package com.sos.joc.model.jobstreams;

import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "event",
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
     *     The event
     */
    @JsonProperty("event")
    public String getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

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
     *     The session
     */
    @JsonProperty("session")
    public String getSession() {
        return session;
    }

    /**
     * 
     * @param session
     *     The session
     */
    @JsonProperty("session")
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The outConditionId
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
     * @param outConditionId
     *     The outConditionId
     */
    @JsonProperty("outConditionId")
    public void setOutConditionId(Long outConditionId) {
        this.outConditionId = outConditionId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(event).append(path).append(jobStream).append(session).append(outConditionId).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(event, rhs.event).append(path, rhs.path).append(jobStream, rhs.jobStream).append(session, rhs.session).append(outConditionId, rhs.outConditionId).isEquals();
    }

}
