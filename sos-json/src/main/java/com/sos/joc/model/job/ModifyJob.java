
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.classes.Latin1ToUtf8;
import com.sos.joc.model.calendar.Calendar;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job modify
 * <p>
 * the command is part of the web servive url
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "job",
    "runTime",
    "runTimeXml",
    "calendars"
})
public class ModifyJob {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("job")
    private String job;
    @JsonProperty("runTime")
    private com.sos.joc.model.joe.schedule.RunTime runTime;
    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     */
    @JsonProperty("runTimeXml")
    private String runTimeXml;
    @JsonProperty("calendars")
    private List<Calendar> calendars = new ArrayList<Calendar>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = Latin1ToUtf8.convert(job);
    }
    
    /**
     * 
     * @return
     *     The runTime
     */
    @JsonProperty("runTime")
    public com.sos.joc.model.joe.schedule.RunTime getRunTime() {
        return runTime;
    }

    /**
     * 
     * @param runTime
     *     The runTime
     */
    @JsonProperty("runTime")
    public void setRunTime(com.sos.joc.model.joe.schedule.RunTime runTime) {
        this.runTime = runTime;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @return
     *     The runTimeXml
     */
    @JsonProperty("runTimeXml")
    public String getRunTimeXml() {
        return runTimeXml;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @param runTimeXml
     *     The runTimeXml
     */
    @JsonProperty("runTimeXml")
    public void setRunTimeXml(String runTimeXml) {
        this.runTimeXml = runTimeXml;
    }

    /**
     * 
     * @return
     *     The calendars
     */
    @JsonProperty("calendars")
    public List<Calendar> getCalendars() {
        return calendars;
    }

    /**
     * 
     * @param calendars
     *     The calendars
     */
    @JsonProperty("calendars")
    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(runTime).append(calendars).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyJob) == false) {
            return false;
        }
        ModifyJob rhs = ((ModifyJob) other);
        return new EqualsBuilder().append(job, rhs.job).append(runTime, rhs.runTime).append(calendars, rhs.calendars).isEquals();
    }

}
