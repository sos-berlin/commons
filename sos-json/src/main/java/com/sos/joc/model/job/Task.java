
package com.sos.joc.model.job;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.order.OrderV;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taskId",
    "pid",
    "state",
    "plannedStart",
    "startedAt",
    "enqueued",
    "idleSince",
    "inProcessSince",
    "steps",
    "_cause",
    "order"
})
public class Task {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    private String taskId;
    @JsonProperty("pid")
    private Integer pid;
    /**
     * task state
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private TaskState state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStart")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date plannedStart;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startedAt")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date startedAt;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("enqueued")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date enqueued;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("idleSince")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date idleSince;
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
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("steps")
    private Integer steps;
    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("_cause")
    @JsonPropertyDescription("For order jobs only cause=order possible")
    private TaskCause _cause;
    /**
     * order (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("order")
    private OrderV order;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    public String getTaskId() {
        return taskId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @JsonProperty("pid")
    public Integer getPid() {
        return pid;
    }

    @JsonProperty("pid")
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * task state
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    public TaskState getState() {
        return state;
    }

    /**
     * task state
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    public void setState(TaskState state) {
        this.state = state;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStart")
    public Date getPlannedStart() {
        return plannedStart;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStart")
    public void setPlannedStart(Date plannedStart) {
        this.plannedStart = plannedStart;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startedAt")
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startedAt")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("enqueued")
    public Date getEnqueued() {
        return enqueued;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("enqueued")
    public void setEnqueued(Date enqueued) {
        this.enqueued = enqueued;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("idleSince")
    public Date getIdleSince() {
        return idleSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("idleSince")
    public void setIdleSince(Date idleSince) {
        this.idleSince = idleSince;
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

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("steps")
    public Integer getSteps() {
        return steps;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("steps")
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("_cause")
    public TaskCause get_cause() {
        return _cause;
    }

    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("_cause")
    public void set_cause(TaskCause _cause) {
        this._cause = _cause;
    }

    /**
     * order (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("order")
    public OrderV getOrder() {
        return order;
    }

    /**
     * order (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("order")
    public void setOrder(OrderV order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("taskId", taskId).append("pid", pid).append("state", state).append("plannedStart", plannedStart).append("startedAt", startedAt).append("enqueued", enqueued).append("idleSince", idleSince).append("inProcessSince", inProcessSince).append("steps", steps).append("_cause", _cause).append("order", order).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(idleSince).append(inProcessSince).append(enqueued).append(_cause).append(startedAt).append(pid).append(state).append(plannedStart).append(steps).append(taskId).append(order).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Task) == false) {
            return false;
        }
        Task rhs = ((Task) other);
        return new EqualsBuilder().append(idleSince, rhs.idleSince).append(inProcessSince, rhs.inProcessSince).append(enqueued, rhs.enqueued).append(_cause, rhs._cause).append(startedAt, rhs.startedAt).append(pid, rhs.pid).append(state, rhs.state).append(plannedStart, rhs.plannedStart).append(steps, rhs.steps).append(taskId, rhs.taskId).append(order, rhs.order).isEquals();
    }

}
