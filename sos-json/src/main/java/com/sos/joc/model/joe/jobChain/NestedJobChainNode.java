
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
 * nested jobChainNode
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_chain_node.job_chain")
@JsonPropertyOrder({
    "jobChain",
    "nextState",
    "errorState"
})
public class NestedJobChainNode
    extends JobChainEndNode
{

    /**
     * path to job chain
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("path to job chain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    private String jobChain;
    /**
     * name of the next job chain node in successful case
     * 
     */
    @JsonProperty("nextState")
    @JsonPropertyDescription("name of the next job chain node in successful case")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    private String nextState;
    /**
     * name of the next job chain node in errornous case
     * 
     */
    @JsonProperty("errorState")
    @JsonPropertyDescription("name of the next job chain node in errornous case")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    private String errorState;

    /**
     * No args constructor for use in serialization
     * 
     */
    public NestedJobChainNode() {
    }

    /**
     * 
     * @param errorState
     * @param jobChain
     * @param nextState
     */
    public NestedJobChainNode(String jobChain, String nextState, String errorState) {
        super();
        this.jobChain = jobChain;
        this.nextState = nextState;
        this.errorState = errorState;
    }

    /**
     * path to job chain
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path to job chain
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
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
     * name of the next job chain node in errornous case
     * 
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public String getErrorState() {
        return errorState;
    }

    /**
     * name of the next job chain node in errornous case
     * 
     */
    @JsonProperty("errorState")
    @JacksonXmlProperty(localName = "error_state", isAttribute = true)
    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("jobChain", jobChain).append("nextState", nextState).append("errorState", errorState).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(jobChain).append(nextState).append(errorState).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NestedJobChainNode) == false) {
            return false;
        }
        NestedJobChainNode rhs = ((NestedJobChainNode) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(jobChain, rhs.jobChain).append(nextState, rhs.nextState).append(errorState, rhs.errorState).isEquals();
    }

}
