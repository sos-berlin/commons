
package com.sos.joc.model.joe.schedule;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * runTime
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "run_time")
@JsonPropertyOrder({
    "schedule"
})
public class RunTime
    extends AbstractSchedule
    implements IJSObject
{

    /**
     * path to a schedule
     * 
     */
    @JsonProperty("schedule")
    @JsonPropertyDescription("path to a schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    private String schedule;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RunTime() {
    }

    /**
     * 
     * @param letRun
     * @param ats
     * @param weekdays
     * @param timeZone
     * @param dates
     * @param runOnce
     * @param schedule
     * @param month
     * @param holidays
     * @param calendars
     * @param periods
     * @param end
     * @param monthdays
     * @param begin
     * @param ultimos
     */
    public RunTime(String schedule, String timeZone, String begin, String end, String letRun, String runOnce, List<Period> periods, List<At> ats, List<Date> dates, Weekdays weekdays, Monthdays monthdays, Ultimos ultimos, List<Month> month, Holidays holidays, String calendars) {
        super(timeZone, begin, end, letRun, runOnce, periods, ats, dates, weekdays, monthdays, ultimos, month, holidays, calendars);
        this.schedule = schedule;
    }

    /**
     * path to a schedule
     * 
     */
    @JsonProperty("schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    public String getSchedule() {
        return schedule;
    }

    /**
     * path to a schedule
     * 
     */
    @JsonProperty("schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("schedule", schedule).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(schedule).toHashCode();
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
        return new EqualsBuilder().appendSuper(super.equals(other)).append(schedule, rhs.schedule).isEquals();
    }

}
