
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.classes.Latin1ToUtf8;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "job",
    "taskIds"
})
public class TasksFilter {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String job;
    @JsonProperty("taskIds")
    private List<TaskId> taskIds = new ArrayList<TaskId>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = Latin1ToUtf8.convert(job);
    }

    @JsonProperty("taskIds")
    public List<TaskId> getTaskIds() {
        return taskIds;
    }

    @JsonProperty("taskIds")
    public void setTaskIds(List<TaskId> taskIds) {
        this.taskIds = taskIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("job", job).append("taskIds", taskIds).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(taskIds).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TasksFilter) == false) {
            return false;
        }
        TasksFilter rhs = ((TasksFilter) other);
        return new EqualsBuilder().append(job, rhs.job).append(taskIds, rhs.taskIds).isEquals();
    }

}
