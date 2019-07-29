
package com.sos.joc.model.conditions;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Out-Condition-Event
 * <p>
 * Out Condition Event
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "command",
    "event",
    "exists",
    "existsInJobStream",
    "jobStream"
})
public class OutConditionEvent {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    @JsonProperty("command")
    private String command;
    @JsonProperty("event")
    private String event;
    @JsonProperty("exists")
    private Boolean exists;
    @JsonProperty("existsInJobStream")
    private Boolean existsInJobStream;
    @JsonProperty("jobStream")
    private String jobStream;

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
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
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The command
     */
    @JsonProperty("command")
    public String getCommand() {
        return command;
    }

    /**
     * 
     * @param command
     *     The command
     */
    @JsonProperty("command")
    public void setCommand(String command) {
        this.command = command;
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
     *     The exists
     */
    @JsonProperty("exists")
    public Boolean getExists() {
        return exists;
    }

    /**
     * 
     * @param exists
     *     The exists
     */
    @JsonProperty("exists")
    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    /**
     * 
     * @return
     *     The existsInJobStream
     */
    @JsonProperty("existsInJobStream")
    public Boolean getExistsInJobStream() {
        return existsInJobStream;
    }

    /**
     * 
     * @param existsInJobStream
     *     The existsInJobStream
     */
    @JsonProperty("existsInJobStream")
    public void setExistsInJobStream(Boolean existsInJobStream) {
        this.existsInJobStream = existsInJobStream;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(command).append(event).append(exists).append(existsInJobStream).append(jobStream).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutConditionEvent) == false) {
            return false;
        }
        OutConditionEvent rhs = ((OutConditionEvent) other);
        return new EqualsBuilder().append(id, rhs.id).append(command, rhs.command).append(event, rhs.event).append(exists, rhs.exists).append(existsInJobStream, rhs.existsInJobStream).append(jobStream, rhs.jobStream).isEquals();
    }

}
