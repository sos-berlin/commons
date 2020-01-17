
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
 * orderPath
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobChain",
    "orderId",
    "state"
})
public class OrderPath {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String jobChain;
    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     */
    @JsonProperty("orderId")
    @JsonPropertyDescription("if orderId undefined or empty then all orders of specified job chain are requested")
    private String orderId;
    /**
     * name of job chain node.
     * 
     */
    @JsonProperty("state")
    @JsonPropertyDescription("name of job chain node.")
    private String state;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = Latin1ToUtf8.convert(jobChain);
    }

    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = Latin1ToUtf8.convert(orderId);
    }

    /**
     * name of job chain node.
     * 
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * name of job chain node.
     * 
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobChain", jobChain).append("orderId", orderId).append("state", state).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChain).append(state).append(orderId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderPath) == false) {
            return false;
        }
        OrderPath rhs = ((OrderPath) other);
        return new EqualsBuilder().append(jobChain, rhs.jobChain).append(state, rhs.state).append(orderId, rhs.orderId).isEquals();
    }

}
