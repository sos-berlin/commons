
package com.sos.joc.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job summary
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pending",
    "running",
    "stopped",
    "waitingForResource",
    "tasks",
    "queuedTasks"
})
public class JobsSummary {

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("pending")
    private Integer pending;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("running")
    private Integer running;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("stopped")
    private Integer stopped;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("waitingForResource")
    private Integer waitingForResource;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    private Integer tasks;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("queuedTasks")
    private Integer queuedTasks;

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("pending")
    public Integer getPending() {
        return pending;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("pending")
    public void setPending(Integer pending) {
        this.pending = pending;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("running")
    public Integer getRunning() {
        return running;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("running")
    public void setRunning(Integer running) {
        this.running = running;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("stopped")
    public Integer getStopped() {
        return stopped;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("stopped")
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("waitingForResource")
    public Integer getWaitingForResource() {
        return waitingForResource;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("waitingForResource")
    public void setWaitingForResource(Integer waitingForResource) {
        this.waitingForResource = waitingForResource;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    public Integer getTasks() {
        return tasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("queuedTasks")
    public Integer getQueuedTasks() {
        return queuedTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("queuedTasks")
    public void setQueuedTasks(Integer queuedTasks) {
        this.queuedTasks = queuedTasks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("pending", pending).append("running", running).append("stopped", stopped).append("waitingForResource", waitingForResource).append("tasks", tasks).append("queuedTasks", queuedTasks).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(running).append(waitingForResource).append(stopped).append(pending).append(tasks).append(queuedTasks).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsSummary) == false) {
            return false;
        }
        JobsSummary rhs = ((JobsSummary) other);
        return new EqualsBuilder().append(running, rhs.running).append(waitingForResource, rhs.waitingForResource).append(stopped, rhs.stopped).append(pending, rhs.pending).append(tasks, rhs.tasks).append(queuedTasks, rhs.queuedTasks).isEquals();
    }

}
