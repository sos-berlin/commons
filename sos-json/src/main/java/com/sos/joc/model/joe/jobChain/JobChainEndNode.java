
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
 * jobChainEndNode
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_chain_node.end")
@JsonPropertyOrder({
    "state"
})
public class JobChainEndNode {

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JsonPropertyDescription("name of the job chain node")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JobChainEndNode() {
    }

    /**
     * 
     * @param state
     */
    public JobChainEndNode(String state) {
        super();
        this.state = state;
    }

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("state", state).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainEndNode) == false) {
            return false;
        }
        JobChainEndNode rhs = ((JobChainEndNode) other);
        return new EqualsBuilder().append(state, rhs.state).isEquals();
    }

}
