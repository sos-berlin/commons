
package com.sos.joc.model.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobStream"
})
public class JobStreamPath {

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("jobStream")
    @JsonPropertyDescription("path with possibly % for sql like syntax in search requests")
    private String jobStream;

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobStream", jobStream).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStream).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobStreamPath) == false) {
            return false;
        }
        JobStreamPath rhs = ((JobStreamPath) other);
        return new EqualsBuilder().append(jobStream, rhs.jobStream).isEquals();
    }

}
