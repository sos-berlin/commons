
package com.sos.joc.model.jobstreams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Conditions Events Filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "session",
    "path",
    "jobStream",
    "outConditionId",
    "limit"
})
public class ConditionEventsFilter {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("session")
    private String session;
    @JsonProperty("path")
    private String path;
    @JsonProperty("jobStream")
    private String jobStream;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("outConditionId")
    private Long outConditionId;
    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    @JsonPropertyDescription("restricts the number of responsed records; -1=unlimited")
    private Integer limit = 0;

    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    @JsonProperty("session")
    public String getSession() {
        return session;
    }

    @JsonProperty("session")
    public void setSession(String session) {
        this.session = session;
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

    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("session", session).append("path", path).append("jobStream", jobStream).append("outConditionId", outConditionId).append("limit", limit).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(session).append(jobStream).append(limit).append(jobschedulerId).append(outConditionId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConditionEventsFilter) == false) {
            return false;
        }
        ConditionEventsFilter rhs = ((ConditionEventsFilter) other);
        return new EqualsBuilder().append(path, rhs.path).append(session, rhs.session).append(jobStream, rhs.jobStream).append(limit, rhs.limit).append(jobschedulerId, rhs.jobschedulerId).append(outConditionId, rhs.outConditionId).isEquals();
    }

}
