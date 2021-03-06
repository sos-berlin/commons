
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.HistoryStateText;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobsFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "jobs",
    "excludeJobs",
    "orders",
    "compact",
    "compactView",
    "withoutDuration",
    "regex",
    "isOrderJob",
    "dateFrom",
    "dateTo",
    "criticality",
    "timeZone",
    "folders",
    "states",
    "limit",
    "historyStates",
    "taskIds",
    "historyIds",
    "runTimeIsTemporary"
})
public class JobsFilter {

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobs")
    private List<JobPath> jobs = new ArrayList<JobPath>();
    @JsonProperty("excludeJobs")
    private List<JobPath> excludeJobs = new ArrayList<JobPath>();
    @JsonProperty("orders")
    private List<OrderPath> orders = new ArrayList<OrderPath>();
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    @JsonPropertyDescription("controls if the object view is compact or detailed")
    private Boolean compact = false;
    @JsonProperty("compactView")
    private Boolean compactView = false;
    /**
     * withoutDuration parameter
     * <p>
     * controls if the duration should be estimated
     * 
     */
    @JsonProperty("withoutDuration")
    @JsonPropertyDescription("controls if the duration should be estimated")
    private Boolean withoutDuration = false;
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    @JsonPropertyDescription("regular expression to filter JobScheduler objects by matching the path")
    private String regex;
    @JsonProperty("isOrderJob")
    private Boolean isOrderJob;
    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateFrom")
    @JsonPropertyDescription("0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp")
    private String dateFrom;
    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateTo")
    @JsonPropertyDescription("0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp")
    private String dateTo;
    @JsonProperty("criticality")
    private List<JobCriticalityFilter> criticality = new ArrayList<JobCriticalityFilter>();
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("timeZone")
    private String timeZone;
    /**
     * folders
     * <p>
     * 
     * 
     */
    @JsonProperty("folders")
    private List<Folder> folders = new ArrayList<Folder>();
    @JsonProperty("states")
    private List<JobStateFilter> states = new ArrayList<JobStateFilter>();
    /**
     * only for db history urls to restrict the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    @JsonPropertyDescription("only for db history urls to restrict the number of responsed records; -1=unlimited")
    private Integer limit = 10000;
    @JsonProperty("historyStates")
    private List<HistoryStateText> historyStates = new ArrayList<HistoryStateText>();
    @JsonProperty("taskIds")
    private List<Long> taskIds = new ArrayList<Long>();
    @JsonProperty("historyIds")
    private List<TaskIdOfOrder> historyIds = new ArrayList<TaskIdOfOrder>();
    @JsonProperty("runTimeIsTemporary")
    private Boolean runTimeIsTemporary;

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * filename
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    @JsonProperty("jobs")
    public List<JobPath> getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(List<JobPath> jobs) {
        this.jobs = jobs;
    }

    @JsonProperty("excludeJobs")
    public List<JobPath> getExcludeJobs() {
        return excludeJobs;
    }

    @JsonProperty("excludeJobs")
    public void setExcludeJobs(List<JobPath> excludeJobs) {
        this.excludeJobs = excludeJobs;
    }

    @JsonProperty("orders")
    public List<OrderPath> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    public void setOrders(List<OrderPath> orders) {
        this.orders = orders;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    public Boolean getCompact() {
        return compact;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    @JsonProperty("compactView")
    public Boolean getCompactView() {
        return compactView;
    }

    @JsonProperty("compactView")
    public void setCompactView(Boolean compactView) {
        this.compactView = compactView;
    }

    /**
     * withoutDuration parameter
     * <p>
     * controls if the duration should be estimated
     * 
     */
    @JsonProperty("withoutDuration")
    public Boolean getWithoutDuration() {
        return withoutDuration;
    }

    /**
     * withoutDuration parameter
     * <p>
     * controls if the duration should be estimated
     * 
     */
    @JsonProperty("withoutDuration")
    public void setWithoutDuration(Boolean withoutDuration) {
        this.withoutDuration = withoutDuration;
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

    @JsonProperty("isOrderJob")
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    @JsonProperty("isOrderJob")
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateFrom")
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateFrom")
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateTo")
    public String getDateTo() {
        return dateTo;
    }

    /**
     * string for dateFrom and dateTo as search filter
     * <p>
     *  0 or [number][smhdwMy] (where smhdwMy unit for second, minute, etc) or ISO 8601 timestamp
     * 
     */
    @JsonProperty("dateTo")
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @JsonProperty("criticality")
    public List<JobCriticalityFilter> getCriticality() {
        return criticality;
    }

    @JsonProperty("criticality")
    public void setCriticality(List<JobCriticalityFilter> criticality) {
        this.criticality = criticality;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("timeZone")
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * string without < and >
     * <p>
     * 
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

    @JsonProperty("states")
    public List<JobStateFilter> getStates() {
        return states;
    }

    @JsonProperty("states")
    public void setStates(List<JobStateFilter> states) {
        this.states = states;
    }

    /**
     * only for db history urls to restrict the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    /**
     * only for db history urls to restrict the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @JsonProperty("historyStates")
    public List<HistoryStateText> getHistoryStates() {
        return historyStates;
    }

    @JsonProperty("historyStates")
    public void setHistoryStates(List<HistoryStateText> historyStates) {
        this.historyStates = historyStates;
    }

    @JsonProperty("taskIds")
    public List<Long> getTaskIds() {
        return taskIds;
    }

    @JsonProperty("taskIds")
    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    @JsonProperty("historyIds")
    public List<TaskIdOfOrder> getHistoryIds() {
        return historyIds;
    }

    @JsonProperty("historyIds")
    public void setHistoryIds(List<TaskIdOfOrder> historyIds) {
        this.historyIds = historyIds;
    }

    @JsonProperty("runTimeIsTemporary")
    public Boolean getRunTimeIsTemporary() {
        return runTimeIsTemporary;
    }

    @JsonProperty("runTimeIsTemporary")
    public void setRunTimeIsTemporary(Boolean runTimeIsTemporary) {
        this.runTimeIsTemporary = runTimeIsTemporary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("jobs", jobs).append("excludeJobs", excludeJobs).append("orders", orders).append("compact", compact).append("compactView", compactView).append("withoutDuration", withoutDuration).append("regex", regex).append("isOrderJob", isOrderJob).append("dateFrom", dateFrom).append("dateTo", dateTo).append("criticality", criticality).append("timeZone", timeZone).append("folders", folders).append("states", states).append("limit", limit).append("historyStates", historyStates).append("taskIds", taskIds).append("historyIds", historyIds).append("runTimeIsTemporary", runTimeIsTemporary).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folders).append(compact).append(jobs).append(withoutDuration).append(historyStates).append(criticality).append(timeZone).append(dateFrom).append(taskIds).append(historyIds).append(compactView).append(states).append(isOrderJob).append(regex).append(dateTo).append(limit).append(orders).append(runTimeIsTemporary).append(jobschedulerId).append(excludeJobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsFilter) == false) {
            return false;
        }
        JobsFilter rhs = ((JobsFilter) other);
        return new EqualsBuilder().append(folders, rhs.folders).append(compact, rhs.compact).append(jobs, rhs.jobs).append(withoutDuration, rhs.withoutDuration).append(historyStates, rhs.historyStates).append(criticality, rhs.criticality).append(timeZone, rhs.timeZone).append(dateFrom, rhs.dateFrom).append(taskIds, rhs.taskIds).append(historyIds, rhs.historyIds).append(compactView, rhs.compactView).append(states, rhs.states).append(isOrderJob, rhs.isOrderJob).append(regex, rhs.regex).append(dateTo, rhs.dateTo).append(limit, rhs.limit).append(orders, rhs.orders).append(runTimeIsTemporary, rhs.runTimeIsTemporary).append(jobschedulerId, rhs.jobschedulerId).append(excludeJobs, rhs.excludeJobs).isEquals();
    }

}
