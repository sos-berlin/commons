
package com.sos.joc.model.plan;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Folder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * plan filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "regex",
    "states",
    "late",
    "dateFrom",
    "dateTo",
    "timeZone",
    "folders",
    "job",
    "jobChain",
    "jobStream",
    "isJobStream",
    "orderId"
})
public class PlanFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    @JsonPropertyDescription("regular expression to filter JobScheduler objects by matching the path")
    private String regex;
    @JsonProperty("states")
    private List<PlanStateText> states = new ArrayList<PlanStateText>();
    @JsonProperty("late")
    private Boolean late;
    @JsonProperty("dateFrom")
    private String dateFrom;
    @JsonProperty("dateTo")
    private String dateTo;
    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     */
    @JsonProperty("timeZone")
    @JsonPropertyDescription("see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones")
    private String timeZone;
    /**
     * folders
     * <p>
     * 
     * 
     */
    @JsonProperty("folders")
    private List<Folder> folders = new ArrayList<Folder>();
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String job;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String jobChain;
    @JsonProperty("jobStream")
    private String jobStream;
    @JsonProperty("isJobStream")
    private Boolean isJobStream;
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    public String getRegex() {
        return regex;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @JsonProperty("states")
    public List<PlanStateText> getStates() {
        return states;
    }

    @JsonProperty("states")
    public void setStates(List<PlanStateText> states) {
        this.states = states;
    }

    @JsonProperty("late")
    public Boolean getLate() {
        return late;
    }

    @JsonProperty("late")
    public void setLate(Boolean late) {
        this.late = late;
    }

    @JsonProperty("dateFrom")
    public String getDateFrom() {
        return dateFrom;
    }

    @JsonProperty("dateFrom")
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    @JsonProperty("dateTo")
    public String getDateTo() {
        return dateTo;
    }

    @JsonProperty("dateTo")
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     */
    @JsonProperty("timeZone")
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     */
    @JsonProperty("timeZone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * folders
     * <p>
     * 
     * 
     */
    @JsonProperty("folders")
    public List<Folder> getFolders() {
        return folders;
    }

    /**
     * folders
     * <p>
     * 
     * 
     */
    @JsonProperty("folders")
    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

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
        this.job = job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
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
     * 
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    @JsonProperty("isJobStream")
    public Boolean getIsJobStream() {
        return isJobStream;
    }

    @JsonProperty("isJobStream")
    public void setIsJobStream(Boolean isJobStream) {
        this.isJobStream = isJobStream;
    }

    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("regex", regex).append("states", states).append("late", late).append("dateFrom", dateFrom).append("dateTo", dateTo).append("timeZone", timeZone).append("folders", folders).append("job", job).append("jobChain", jobChain).append("jobStream", jobStream).append("isJobStream", isJobStream).append("orderId", orderId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(isJobStream).append(folders).append(orderId).append(jobChain).append(jobStream).append(timeZone).append(dateFrom).append(states).append(regex).append(late).append(dateTo).append(jobschedulerId).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanFilter) == false) {
            return false;
        }
        PlanFilter rhs = ((PlanFilter) other);
        return new EqualsBuilder().append(isJobStream, rhs.isJobStream).append(folders, rhs.folders).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(jobStream, rhs.jobStream).append(timeZone, rhs.timeZone).append(dateFrom, rhs.dateFrom).append(states, rhs.states).append(regex, rhs.regex).append(late, rhs.late).append(dateTo, rhs.dateTo).append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).isEquals();
    }

}
