
package com.sos.joc.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job state
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "severity",
    "_text",
    "manually"
})
public class JobState {

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
     * (Required)
     * 
     */
    @JsonProperty("severity")
    @JsonPropertyDescription("0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown")
    private Integer severity;
    /**
     * job state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private JobStateText _text;
    @JsonProperty("manually")
    private Boolean manually;

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
     * (Required)
     * 
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
     * (Required)
     * 
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * job state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    public JobStateText get_text() {
        return _text;
    }

    /**
     * job state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    public void set_text(JobStateText _text) {
        this._text = _text;
    }

    @JsonProperty("manually")
    public Boolean getManually() {
        return manually;
    }

    @JsonProperty("manually")
    public void setManually(Boolean manually) {
        this.manually = manually;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("severity", severity).append("_text", _text).append("manually", manually).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(manually).append(_text).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobState) == false) {
            return false;
        }
        JobState rhs = ((JobState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(manually, rhs.manually).append(_text, rhs._text).isEquals();
    }

}
