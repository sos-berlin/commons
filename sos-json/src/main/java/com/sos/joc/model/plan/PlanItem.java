
package com.sos.joc.model.plan;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * dailyPlanItem
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "surveyDate",
    "job",
    "jobStream",
    "jobChain",
    "orderId",
    "plannedStartTime",
    "expectedEndTime",
    "startTime",
    "endTime",
    "historyId",
    "node",
    "exitCode",
    "error",
    "startMode",
    "period",
    "late",
    "state",
    "auditLogId"
})
public class PlanItem {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    @JsonPropertyDescription("Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String job;
    @JsonProperty("jobStream")
    private String jobStream;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    private String jobChain;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStartTime")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date plannedStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("expectedEndTime")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date expectedEndTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startTime")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("endTime")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date endTime;
    @JsonProperty("historyId")
    private String historyId;
    /**
     * only for orders
     * 
     */
    @JsonProperty("node")
    @JsonPropertyDescription("only for orders")
    private String node;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    private Integer exitCode;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     */
    @JsonProperty("startMode")
    @JsonPropertyDescription("0=single_start, 1=start_start_repeat, 2=start_end_repeat")
    private Integer startMode = 0;
    /**
     * undefined for startMode=0
     * 
     */
    @JsonProperty("period")
    @JsonPropertyDescription("undefined for startMode=0")
    private Period period;
    @JsonProperty("late")
    private Boolean late;
    /**
     * plan state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private PlanState state;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLogId")
    private Long auditLogId;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
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

    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
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

    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStartTime")
    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStartTime")
    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("expectedEndTime")
    public Date getExpectedEndTime() {
        return expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("expectedEndTime")
    public void setExpectedEndTime(Date expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startTime")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startTime")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("endTime")
    public Date getEndTime() {
        return endTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("endTime")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("historyId")
    public String getHistoryId() {
        return historyId;
    }

    @JsonProperty("historyId")
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    /**
     * only for orders
     * 
     */
    @JsonProperty("node")
    public String getNode() {
        return node;
    }

    /**
     * only for orders
     * 
     */
    @JsonProperty("node")
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    public Err getError() {
        return error;
    }

    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    public void setError(Err error) {
        this.error = error;
    }

    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     */
    @JsonProperty("startMode")
    public Integer getStartMode() {
        return startMode;
    }

    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     */
    @JsonProperty("startMode")
    public void setStartMode(Integer startMode) {
        this.startMode = startMode;
    }

    /**
     * undefined for startMode=0
     * 
     */
    @JsonProperty("period")
    public Period getPeriod() {
        return period;
    }

    /**
     * undefined for startMode=0
     * 
     */
    @JsonProperty("period")
    public void setPeriod(Period period) {
        this.period = period;
    }

    @JsonProperty("late")
    public Boolean getLate() {
        return late;
    }

    @JsonProperty("late")
    public void setLate(Boolean late) {
        this.late = late;
    }

    /**
     * plan state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    public PlanState getState() {
        return state;
    }

    /**
     * plan state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    public void setState(PlanState state) {
        this.state = state;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLogId")
    public Long getAuditLogId() {
        return auditLogId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLogId")
    public void setAuditLogId(Long auditLogId) {
        this.auditLogId = auditLogId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("surveyDate", surveyDate).append("job", job).append("jobStream", jobStream).append("jobChain", jobChain).append("orderId", orderId).append("plannedStartTime", plannedStartTime).append("expectedEndTime", expectedEndTime).append("startTime", startTime).append("endTime", endTime).append("historyId", historyId).append("node", node).append("exitCode", exitCode).append("error", error).append("startMode", startMode).append("period", period).append("late", late).append("state", state).append("auditLogId", auditLogId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(period).append(surveyDate).append(orderId).append(jobStream).append(jobChain).append(error).append(node).append(plannedStartTime).append(late).append(historyId).append(startMode).append(auditLogId).append(exitCode).append(expectedEndTime).append(startTime).append(endTime).append(state).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanItem) == false) {
            return false;
        }
        PlanItem rhs = ((PlanItem) other);
        return new EqualsBuilder().append(period, rhs.period).append(surveyDate, rhs.surveyDate).append(orderId, rhs.orderId).append(jobStream, rhs.jobStream).append(jobChain, rhs.jobChain).append(error, rhs.error).append(node, rhs.node).append(plannedStartTime, rhs.plannedStartTime).append(late, rhs.late).append(historyId, rhs.historyId).append(startMode, rhs.startMode).append(auditLogId, rhs.auditLogId).append(exitCode, rhs.exitCode).append(expectedEndTime, rhs.expectedEndTime).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(state, rhs.state).append(job, rhs.job).isEquals();
    }

}
