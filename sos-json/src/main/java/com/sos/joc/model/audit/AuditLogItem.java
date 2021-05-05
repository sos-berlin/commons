
package com.sos.joc.model.audit;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * audit
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account",
    "request",
    "created",
    "jobschedulerId",
    "comment",
    "parameters",
    "job",
    "jobChain",
    "jobStream",
    "orderId",
    "calendar",
    "timeSpent",
    "ticketLink"
})
public class AuditLogItem {

    /**
     * string without < and >
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("account")
    private String account;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("request")
    private String request;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("created")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    private Date created;
    /**
     * filename
     * <p>
     * 
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    private String comment;
    /**
     * JSON object as string, parameter of request
     * 
     */
    @JsonProperty("parameters")
    @JsonPropertyDescription("JSON object as string, parameter of request")
    private String parameters;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    private String job;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    private String jobStream;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("calendar")
    private String calendar;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    private Integer timeSpent;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    private String ticketLink;

    /**
     * string without < and >
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    /**
     * string without < and >
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("request")
    public String getRequest() {
        return request;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("request")
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("created")
    public Date getCreated() {
        return created;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("created")
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * filename
     * <p>
     * 
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
     * 
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * JSON object as string, parameter of request
     * 
     */
    @JsonProperty("parameters")
    public String getParameters() {
        return parameters;
    }

    /**
     * JSON object as string, parameter of request
     * 
     */
    @JsonProperty("parameters")
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("calendar")
    public String getCalendar() {
        return calendar;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("calendar")
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    public Integer getTimeSpent() {
        return timeSpent;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    public String getTicketLink() {
        return ticketLink;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("account", account).append("request", request).append("created", created).append("jobschedulerId", jobschedulerId).append("comment", comment).append("parameters", parameters).append("job", job).append("jobChain", jobChain).append("jobStream", jobStream).append("orderId", orderId).append("calendar", calendar).append("timeSpent", timeSpent).append("ticketLink", ticketLink).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendar).append(request).append(orderId).append(timeSpent).append(created).append(jobChain).append(jobStream).append(ticketLink).append(comment).append(jobschedulerId).append(job).append(parameters).append(account).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AuditLogItem) == false) {
            return false;
        }
        AuditLogItem rhs = ((AuditLogItem) other);
        return new EqualsBuilder().append(calendar, rhs.calendar).append(request, rhs.request).append(orderId, rhs.orderId).append(timeSpent, rhs.timeSpent).append(created, rhs.created).append(jobChain, rhs.jobChain).append(jobStream, rhs.jobStream).append(ticketLink, rhs.ticketLink).append(comment, rhs.comment).append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(parameters, rhs.parameters).append(account, rhs.account).isEquals();
    }

}
