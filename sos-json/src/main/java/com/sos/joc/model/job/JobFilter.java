
package com.sos.joc.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.classes.Latin1ToUtf8;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "job",
    "compact",
    "compactView"
})
public class JobFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String job;
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    @JsonPropertyDescription("controls if the object view is compact or detailed")
    private Boolean compact = false;
    @JsonProperty("compactView")
    private Boolean compactView = false;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = Latin1ToUtf8.convert(job);
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    public Boolean getCompact() {
        return compact;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    @JsonProperty("compactView")
    public Boolean getCompactView() {
        return compactView;
    }

    @JsonProperty("compactView")
    public void setCompactView(Boolean compactView) {
        this.compactView = compactView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("job", job).append("compact", compact).append("compactView", compactView).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(job).append(compact).append(compactView).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobFilter) == false) {
            return false;
        }
        JobFilter rhs = ((JobFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(compact, rhs.compact).append(compactView, rhs.compactView).isEquals();
    }

}
