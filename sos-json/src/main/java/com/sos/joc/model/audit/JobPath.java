
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
    "job"
})
public class JobPath {

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("path with possibly % for sql like syntax in search requests")
    private String job;

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * searchPath
     * <p>
     * path with possibly % for sql like syntax in search requests
     * (Required)
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("job", job).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobPath) == false) {
            return false;
        }
        JobPath rhs = ((JobPath) other);
        return new EqualsBuilder().append(job, rhs.job).isEquals();
    }

}
