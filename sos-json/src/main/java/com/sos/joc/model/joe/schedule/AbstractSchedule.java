
package com.sos.joc.model.joe.schedule;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "timeZone",
    "begin",
    "end",
    "letRun",
    "runOnce",
    "periods",
    "ats",
    "dates",
    "weekdays",
    "monthdays",
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
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    private String begin;
    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    private String end;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    private String letRun;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("runOnce")
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
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekdays", isAttribute = false)
    private Weekdays weekdays;
    /**
     * monthdays
     * <p>
     * 
     * 
     */
    @JsonProperty("monthdays")
    @JacksonXmlProperty(localName = "monthdays", isAttribute = false)
    private Monthdays monthdays;
    /**
     * ultimos
     * <p>
     * 
     * 
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    private Ultimos ultimos;
    /**
     * months
     * <p>
     * 
     * 
     */
    @JsonProperty("month")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    private List<Month> month = null;
    /**
     * holidays
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

    /**
     * No args constructor for use in serialization
     * 
     */
    public AbstractSchedule() {
    }

    /**
     * 
     * @param letRun
     * @param ats
     * @param weekdays
     * @param timeZone
     * @param dates
     * @param runOnce
     * @param month
     * @param holidays
     * @param calendars
     * @param periods
     * @param end
     * @param monthdays
     * @param begin
     * @param ultimos
     */
    public AbstractSchedule(String timeZone, String begin, String end, String letRun, String runOnce, List<Period> periods, List<At> ats, List<Date> dates, Weekdays weekdays, Monthdays monthdays, Ultimos ultimos, List<Month> month, Holidays holidays, String calendars) {
        this.timeZone = timeZone;
        this.begin = begin;
        this.end = end;
        this.letRun = letRun;
        this.runOnce = runOnce;
        this.periods = periods;
        this.ats = ats;
        this.dates = dates;
        this.weekdays = weekdays;
        this.monthdays = monthdays;
        this.ultimos = ultimos;
        this.month = month;
        this.holidays = holidays;
        this.calendars = calendars;
    }

    /**
     * 
     * @return
     *     The timeZone
     */
    @JsonProperty("timeZone")
    @JacksonXmlProperty(localName = "time_zone", isAttribute = true)
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * 
     * @param timeZone
     *     The timeZone
     */
    @JsonProperty("timeZone")
    @JacksonXmlProperty(localName = "time_zone", isAttribute = true)
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @return
     *     The begin
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public String getBegin() {
        return begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @param begin
     *     The begin
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @return
     *     The end
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public String getEnd() {
        return end;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @param end
     *     The end
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The letRun
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public String getLetRun() {
        return letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param letRun
     *     The letRun
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public void setLetRun(String letRun) {
        this.letRun = letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The runOnce
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "once", isAttribute = true)
    public String getRunOnce() {
        return runOnce;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param runOnce
     *     The runOnce
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
     * @return
     *     The periods
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
     * @param periods
     *     The periods
     */
    @JsonProperty("periods")
    @JacksonXmlProperty(localName = "period", isAttribute = false)
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    /**
     * 
     * @return
     *     The ats
     */
    @JsonProperty("ats")
    @JacksonXmlProperty(localName = "at", isAttribute = false)
    public List<At> getAts() {
        return ats;
    }

    /**
     * 
     * @param ats
     *     The ats
     */
    @JsonProperty("ats")
    @JacksonXmlProperty(localName = "at", isAttribute = false)
    public void setAts(List<At> ats) {
        this.ats = ats;
    }

    /**
     * 
     * @return
     *     The dates
     */
    @JsonProperty("dates")
    @JacksonXmlProperty(localName = "date", isAttribute = false)
    public List<Date> getDates() {
        return dates;
    }

    /**
     * 
     * @param dates
     *     The dates
     */
    @JsonProperty("dates")
    @JacksonXmlProperty(localName = "date", isAttribute = false)
    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    /**
     * weekdays
     * <p>
     * 
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekdays", isAttribute = false)
    public Weekdays getWeekdays() {
        return weekdays;
    }

    /**
     * weekdays
     * <p>
     * 
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekdays", isAttribute = false)
    public void setWeekdays(Weekdays weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     * @return
     *     The monthdays
     */
    @JsonProperty("monthdays")
    @JacksonXmlProperty(localName = "monthdays", isAttribute = false)
    public Monthdays getMonthdays() {
        return monthdays;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     * @param monthdays
     *     The monthdays
     */
    @JsonProperty("monthdays")
    @JacksonXmlProperty(localName = "monthdays", isAttribute = false)
    public void setMonthdays(Monthdays monthdays) {
        this.monthdays = monthdays;
    }

    /**
     * ultimos
     * <p>
     * 
     * 
     * @return
     *     The ultimos
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    public Ultimos getUltimos() {
        return ultimos;
    }

    /**
     * ultimos
     * <p>
     * 
     * 
     * @param ultimos
     *     The ultimos
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    public void setUltimos(Ultimos ultimos) {
        this.ultimos = ultimos;
    }

    /**
     * months
     * <p>
     * 
     * 
     * @return
     *     The month
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    public List<Month> getMonth() {
        return month;
    }

    /**
     * months
     * <p>
     * 
     * 
     * @param month
     *     The month
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = false)
    public void setMonth(List<Month> month) {
        this.month = month;
    }

    /**
     * holidays
     * <p>
     * 
     * 
     * @return
     *     The holidays
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public Holidays getHolidays() {
        return holidays;
    }

    /**
     * holidays
     * <p>
     * 
     * 
     * @param holidays
     *     The holidays
     */
    @JsonProperty("holidays")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public void setHolidays(Holidays holidays) {
        this.holidays = holidays;
    }

    /**
     * 
     * @return
     *     The calendars
     */
    @JsonProperty("calendars")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "calendars", isAttribute = false)
    public String getCalendars() {
        return calendars;
    }

    /**
     * 
     * @param calendars
     *     The calendars
     */
    @JsonProperty("calendars")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "calendars", isAttribute = false)
    public void setCalendars(String calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timeZone).append(begin).append(end).append(letRun).append(runOnce).append(periods).append(ats).append(dates).append(weekdays).append(monthdays).append(ultimos).append(month).append(holidays).append(calendars).toHashCode();
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
        return new EqualsBuilder().append(timeZone, rhs.timeZone).append(begin, rhs.begin).append(end, rhs.end).append(letRun, rhs.letRun).append(runOnce, rhs.runOnce).append(periods, rhs.periods).append(ats, rhs.ats).append(dates, rhs.dates).append(weekdays, rhs.weekdays).append(monthdays, rhs.monthdays).append(ultimos, rhs.ultimos).append(month, rhs.month).append(holidays, rhs.holidays).append(calendars, rhs.calendars).isEquals();
    }

}
