
package com.sos.joc.model.job;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order in task
 * <p>
 * Only relevant for order jobs; cause=order resp.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "path",
    "orderId",
    "jobChain",
    "title",
    "state",
    "inProcessSince"
})
public class OrderOfTask {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
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
    @JsonProperty("title")
    private String title;
    /**
     * name of the current node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JsonPropertyDescription("name of the current node")
    private String state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("inProcessSince")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date inProcessSince;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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
        this.jobChain = jobChain;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * name of the current node
     * (Required)
     * 
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * name of the current node
     * (Required)
     * 
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("inProcessSince")
    public Date getInProcessSince() {
        return inProcessSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("inProcessSince")
    public void setInProcessSince(Date inProcessSince) {
        this.inProcessSince = inProcessSince;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("path", path).append("orderId", orderId).append("jobChain", jobChain).append("title", title).append("state", state).append("inProcessSince", inProcessSince).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(inProcessSince).append(orderId).append(jobChain).append(state).append(title).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderOfTask) == false) {
            return false;
        }
        OrderOfTask rhs = ((OrderOfTask) other);
        return new EqualsBuilder().append(path, rhs.path).append(inProcessSince, rhs.inProcessSince).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(state, rhs.state).append(title, rhs.title).isEquals();
    }

}
