
package com.sos.joc.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.classes.Latin1ToUtf8;
import com.sos.joc.model.common.ConfigurationMime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobConfigurationFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "job",
    "mime"
})
public class JobConfigurationFilter {

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
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    @JsonPropertyDescription("The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.")
    private ConfigurationMime mime = ConfigurationMime.fromValue("XML");

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
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    public ConfigurationMime getMime() {
        return mime;
    }

    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    public void setMime(ConfigurationMime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("job", job).append("mime", mime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(job).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobConfigurationFilter) == false) {
            return false;
        }
        JobConfigurationFilter rhs = ((JobConfigurationFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(mime, rhs.mime).isEquals();
    }

}
