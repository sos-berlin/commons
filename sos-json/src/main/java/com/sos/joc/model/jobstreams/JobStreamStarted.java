
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.NameValuePair;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobStreamStartJob
 * <p>
 * Reset Workflow, unconsume expressions
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "session",
    "params"
})
public class JobStreamStarted {

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("session")
    private String session;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

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
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("session", session).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(params).append(session).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamStarted) == false) {
            return false;
        }
        JobStreamStarted rhs = ((JobStreamStarted) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(params, rhs.params).append(session, rhs.session).isEquals();
    }

}
