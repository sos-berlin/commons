
package com.sos.joc.model.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.calendar.Calendar;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * runtime
 * <p>
 * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "surveyDate",
    "runTime",
    "runTimeXml",
    "permanentRunTime",
    "runTimeIsTemporary",
    "calendars"
})
public class RunTime {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    @JsonPropertyDescription("Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    private Date surveyDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTime")
    private com.sos.joc.model.joe.schedule.RunTime runTime;
    /**
     * is required iff runTimeIsTemporary = true
     * 
     */
    @JsonProperty("runTimeXml")
    private String runTimeXml;
    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("permanentRunTime")
    @JsonPropertyDescription("is required iff runTimeIsTemporary = true")
    private String permanentRunTime;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTimeIsTemporary")
    private Boolean runTimeIsTemporary = false;
    @JsonProperty("calendars")
    private List<Calendar> calendars = new ArrayList<Calendar>();

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    public com.sos.joc.model.joe.schedule.RunTime getRunTime() {
        return runTime;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    public void setRunTime(com.sos.joc.model.joe.schedule.RunTime runTime) {
        this.runTime = runTime;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTimeXml")
    public String getRunTimeXml() {
        return runTimeXml;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTimeXml")
    public void setRunTimeXml(String runTimeXml) {
        this.runTimeXml = runTimeXml;
    }

    /**
     * is required iff runTimeIsTemporary = true
     * 
     */
    @JsonProperty("permanentRunTime")
    public String getPermanentRunTime() {
        return permanentRunTime;
    }

    /**
     * is required iff runTimeIsTemporary = true
     * 
     */
    @JsonProperty("permanentRunTime")
    public void setPermanentRunTime(String permanentRunTime) {
        this.permanentRunTime = permanentRunTime;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTimeIsTemporary")
    public Boolean getRunTimeIsTemporary() {
        return runTimeIsTemporary;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("runTimeIsTemporary")
    public void setRunTimeIsTemporary(Boolean runTimeIsTemporary) {
        this.runTimeIsTemporary = runTimeIsTemporary;
    }

    @JsonProperty("calendars")
    public List<Calendar> getCalendars() {
        return calendars;
    }

    @JsonProperty("calendars")
    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("surveyDate", surveyDate).append("runTime", runTime).append("runTimeXml", runTimeXml).append("permanentRunTime", permanentRunTime).append("runTimeIsTemporary", runTimeIsTemporary).append("calendars", calendars).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(runTimeXml).append(permanentRunTime).append(surveyDate).append(calendars).append(runTime).append(runTimeIsTemporary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RunTime) == false) {
            return false;
        }
        RunTime rhs = ((RunTime) other);
        return new EqualsBuilder().append(runTimeXml, rhs.runTimeXml).append(permanentRunTime, rhs.permanentRunTime).append(surveyDate, rhs.surveyDate).append(calendars, rhs.calendars).append(runTime, rhs.runTime).append(runTimeIsTemporary, rhs.runTimeIsTemporary).isEquals();
    }

}
