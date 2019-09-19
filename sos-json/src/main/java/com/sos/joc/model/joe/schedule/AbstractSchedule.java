
package com.sos.joc.model.joe.schedule;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * super class for schedule and run_time
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "abstract_schedule")
@JsonPropertyOrder({
    "timeZone",
    "begin",
    "end",
    "letRun",
    "runOnce",
    "periods",
    "ats",
    "dates",
    "weekDays",
    "monthDays",
    "ultimos",
    "month",
    "holidays",
    "calendars"
})
public abstract class AbstractSchedule {

    @JsonProperty("timeZone")
    @JacksonXmlProperty(localName = "time_zone", isAttribute = true)
    private String timeZone;
    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("begin")
    @JsonPropertyDescription("pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    private String begin;
    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("end")
    @JsonPropertyDescription("pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    private String end;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("letRun")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    private String letRun;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("runOnce")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "once", isAttribute = true)
    private String runOnce;
    /**
     * periods
     * <p>
     * 
     * 
     */
    @JsonProperty("periods")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "period", isAttribute = false)
    private List<Period> periods = null;
    @JsonProperty("ats")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "at", isAttribute = false)
    private List<At> ats = null;
    @JsonProperty("dates")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "date", isAttribute = false)
    private List<Date> dates = null;
    /**
     * TODO
     * 
     */
    @JsonProperty("weekDays")
    @JsonPropertyDescription("TODO")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    private String weekDays;
    /**
     * TODO
     * 
     */
    @JsonProperty("monthDays")
    @JsonPropertyDescription("TODO")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    private String monthDays;
    /**
     * TODO
     * 
     */
    @JsonProperty("ultimos")
    @JsonPropertyDescription("TODO")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    private String ultimos;
    /**
     * TODO
     * 
     */
    @JsonProperty("month")
    @JsonPropertyDescription("TODO")
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    private String month;
    /**
     * holidays without weekdays elements TODO?
     * <p>
     * 
     * 
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    private Holidays holidays;
    @JsonProperty("calendars")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "calendars", isAttribute = false)
    private String calendars;

    @JsonProperty("timeZone")
    @JacksonXmlProperty(localName = "time_zone", isAttribute = true)
    public String getTimeZone() {
        return timeZone;
    }

    @JsonProperty("timeZone")
    @JacksonXmlProperty(localName = "time_zone", isAttribute = true)
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public String getBegin() {
        return begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public String getEnd() {
        return end;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public String getLetRun() {
        return letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public void setLetRun(String letRun) {
        this.letRun = letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "once", isAttribute = true)
    public String getRunOnce() {
        return runOnce;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "once", isAttribute = true)
    public void setRunOnce(String runOnce) {
        this.runOnce = runOnce;
    }

    /**
     * periods
     * <p>
     * 
     * 
     */
    @JsonProperty("periods")
    @JacksonXmlProperty(localName = "period", isAttribute = false)
    public List<Period> getPeriods() {
        return periods;
    }

    /**
     * periods
     * <p>
     * 
     * 
     */
    @JsonProperty("periods")
    @JacksonXmlProperty(localName = "period", isAttribute = false)
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    @JsonProperty("ats")
    @JacksonXmlProperty(localName = "at", isAttribute = false)
    public List<At> getAts() {
        return ats;
    }

    @JsonProperty("ats")
    @JacksonXmlProperty(localName = "at", isAttribute = false)
    public void setAts(List<At> ats) {
        this.ats = ats;
    }

    @JsonProperty("dates")
    @JacksonXmlProperty(localName = "date", isAttribute = false)
    public List<Date> getDates() {
        return dates;
    }

    @JsonProperty("dates")
    @JacksonXmlProperty(localName = "date", isAttribute = false)
    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    public String getWeekDays() {
        return weekDays;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("monthDays")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    public String getMonthDays() {
        return monthDays;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("monthDays")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    public void setMonthDays(String monthDays) {
        this.monthDays = monthDays;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    public String getUltimos() {
        return ultimos;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    public void setUltimos(String ultimos) {
        this.ultimos = ultimos;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    public String getMonth() {
        return month;
    }

    /**
     * TODO
     * 
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * holidays without weekdays elements TODO?
     * <p>
     * 
     * 
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public Holidays getHolidays() {
        return holidays;
    }

    /**
     * holidays without weekdays elements TODO?
     * <p>
     * 
     * 
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public void setHolidays(Holidays holidays) {
        this.holidays = holidays;
    }

    @JsonProperty("calendars")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "calendars", isAttribute = false)
    public String getCalendars() {
        return calendars;
    }

    @JsonProperty("calendars")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "calendars", isAttribute = false)
    public void setCalendars(String calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("timeZone", timeZone).append("begin", begin).append("end", end).append("letRun", letRun).append("runOnce", runOnce).append("periods", periods).append("ats", ats).append("dates", dates).append("weekDays", weekDays).append("monthDays", monthDays).append("ultimos", ultimos).append("month", month).append("holidays", holidays).append("calendars", calendars).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(letRun).append(ats).append(monthDays).append(timeZone).append(dates).append(runOnce).append(month).append(holidays).append(weekDays).append(calendars).append(periods).append(end).append(begin).append(ultimos).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AbstractSchedule) == false) {
            return false;
        }
        AbstractSchedule rhs = ((AbstractSchedule) other);
        return new EqualsBuilder().append(letRun, rhs.letRun).append(ats, rhs.ats).append(monthDays, rhs.monthDays).append(timeZone, rhs.timeZone).append(dates, rhs.dates).append(runOnce, rhs.runOnce).append(month, rhs.month).append(holidays, rhs.holidays).append(weekDays, rhs.weekDays).append(calendars, rhs.calendars).append(periods, rhs.periods).append(end, rhs.end).append(begin, rhs.begin).append(ultimos, rhs.ultimos).isEquals();
    }

}
