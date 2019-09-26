
package com.sos.joc.model.joe.jobchain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainNode
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_chain_node")
@JsonPropertyOrder({
    "job",
    "nextState",
    "errorState",
    "onError",
    "delay",
    "onReturnCodes"
})
public class JobChainNode
    extends JobChainEndNode
{

    /**
     * path to job
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("path to job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    private String job;
    /**
     * name of the next job chain node in successful case
     * 
     */
    @JsonProperty("nextState")
    @JsonPropertyDescription("name of the next job chain node in successful case")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    private String nextState;
    /**
     * name of the next job chain node in erroneous case
     * 
     */
    @JsonProperty("errorState")
    @JsonPropertyDescription("name of the next job chain node in erroneous case")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    private String errorState;
    /**
     * possible values: suspend and setback
     * 
     */
    @JsonProperty("onError")
    @JsonPropertyDescription("possible values: suspend and setback")
    @JacksonXmlProperty(localName = "on_error", isAttribute = true)
    private String onError;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    private Integer delay;
    @JsonProperty("onReturnCodes")
    @JacksonXmlProperty(localName = "on_return_codes", isAttribute = false)
    private OnReturnCodes onReturnCodes;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JobChainNode() {
    }

    /**
     * 
     * @param onError
     * @param delay
     * @param errorState
     * @param nextState
     * @param job
     * @param onReturnCodes
     */
    public JobChainNode(String job, String nextState, String errorState, String onError, Integer delay, OnReturnCodes onReturnCodes) {
        super();
        this.job = job;
        this.nextState = nextState;
        this.errorState = errorState;
        this.onError = onError;
        this.delay = delay;
        this.onReturnCodes = onReturnCodes;
    }

    /**
     * path to job
     * 
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public String getJob() {
        return job;
    }

    /**
     * path to job
     * 
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * name of the next job chain node in successful case
     * 
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public String getNextState() {
        return nextState;
    }

    /**
     * name of the next job chain node in successful case
     * 
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    /**
     * name of the next job chain node in erroneous case
     * 
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public String getErrorState() {
        return errorState;
    }

    /**
     * name of the next job chain node in erroneous case
     * 
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    /**
     * possible values: suspend and setback
     * 
     */
    @JsonProperty("onError")
    @JacksonXmlProperty(localName = "on_error", isAttribute = true)
    public String getOnError() {
        return onError;
    }

    /**
     * possible values: suspend and setback
     * 
     */
    @JsonProperty("onError")
    @JacksonXmlProperty(localName = "on_error", isAttribute = true)
    public void setOnError(String onError) {
        this.onError = onError;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public Integer getDelay() {
        return delay;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @JsonProperty("onReturnCodes")
    @JacksonXmlProperty(localName = "on_return_codes", isAttribute = false)
    public OnReturnCodes getOnReturnCodes() {
        return onReturnCodes;
    }

    @JsonProperty("onReturnCodes")
    @JacksonXmlProperty(localName = "on_return_codes", isAttribute = false)
    public void setOnReturnCodes(OnReturnCodes onReturnCodes) {
        this.onReturnCodes = onReturnCodes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("job", job).append("nextState", nextState).append("errorState", errorState).append("onError", onError).append("delay", delay).append("onReturnCodes", onReturnCodes).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(onError).append(delay).append(errorState).append(nextState).append(job).append(onReturnCodes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainNode) == false) {
            return false;
        }
        JobChainNode rhs = ((JobChainNode) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(onError, rhs.onError).append(delay, rhs.delay).append(errorState, rhs.errorState).append(nextState, rhs.nextState).append(job, rhs.job).append(onReturnCodes, rhs.onReturnCodes).isEquals();
    }

}
