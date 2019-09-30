
package com.sos.joc.model.joe.jobchain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    private String job;
    /**
     * name of the next job chain node in successful case
     * 
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    private String nextState;
    /**
     * name of the next job chain node in erroneous case
     * 
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    private String errorState;
    /**
     * possible values: suspend and setback
     * 
     */
    @JsonProperty("onError")
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
     * @return
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public String getJob() {
        return job;
    }

    /**
     * path to job
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * name of the next job chain node in successful case
     * 
     * @return
     *     The nextState
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public String getNextState() {
        return nextState;
    }

    /**
     * name of the next job chain node in successful case
     * 
     * @param nextState
     *     The nextState
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    /**
     * name of the next job chain node in erroneous case
     * 
     * @return
     *     The errorState
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public String getErrorState() {
        return errorState;
    }

    /**
     * name of the next job chain node in erroneous case
     * 
     * @param errorState
     *     The errorState
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    /**
     * possible values: suspend and setback
     * 
     * @return
     *     The onError
     */
    @JsonProperty("onError")
    @JacksonXmlProperty(localName = "on_error", isAttribute = true)
    public String getOnError() {
        return onError;
    }

    /**
     * possible values: suspend and setback
     * 
     * @param onError
     *     The onError
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
     * @return
     *     The delay
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
     * @param delay
     *     The delay
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * 
     * @return
     *     The onReturnCodes
     */
    @JsonProperty("onReturnCodes")
    @JacksonXmlProperty(localName = "on_return_codes", isAttribute = false)
    public OnReturnCodes getOnReturnCodes() {
        return onReturnCodes;
    }

    /**
     * 
     * @param onReturnCodes
     *     The onReturnCodes
     */
    @JsonProperty("onReturnCodes")
    @JacksonXmlProperty(localName = "on_return_codes", isAttribute = false)
    public void setOnReturnCodes(OnReturnCodes onReturnCodes) {
        this.onReturnCodes = onReturnCodes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(job).append(nextState).append(errorState).append(onError).append(delay).append(onReturnCodes).toHashCode();
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
        return new EqualsBuilder().appendSuper(super.equals(other)).append(job, rhs.job).append(nextState, rhs.nextState).append(errorState, rhs.errorState).append(onError, rhs.onError).append(delay, rhs.delay).append(onReturnCodes, rhs.onReturnCodes).isEquals();
    }

}
