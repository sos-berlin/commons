
package com.sos.joc.model.joe.schedule;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "month")
@JsonPropertyOrder({
    "month",
    "periods",
    "weekDays",
    "monthDays",
    "ultimos"
})
public class Month {

    /**
     * unordered space seperated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     */
    @JsonProperty("month")
    @JsonPropertyDescription("unordered space seperated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december")
    @JacksonXmlProperty(localName = "month", isAttribute = true)
    private String month;
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
    /**
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    private WeekDays weekDays;
    /**
     * monthdays
     * <p>
     * 
     * 
     */
    @JsonProperty("monthDays")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    private Monthdays monthDays;
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
     * unordered space seperated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = true)
    public String getMonth() {
        return month;
    }

    /**
     * unordered space seperated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = true)
    public void setMonth(String month) {
        this.month = month;
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

    /**
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    public WeekDays getWeekDays() {
        return weekDays;
    }

    /**
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    public void setWeekDays(WeekDays weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     */
    @JsonProperty("monthDays")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    public Monthdays getMonthDays() {
        return monthDays;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     */
    @JsonProperty("monthDays")
    @JacksonXmlProperty(localName = "month_days", isAttribute = false)
    public void setMonthDays(Monthdays monthDays) {
        this.monthDays = monthDays;
    }

    /**
     * ultimos
     * <p>
     * 
     * 
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
     */
    @JsonProperty("ultimos")
    @JacksonXmlProperty(localName = "ultimos", isAttribute = false)
    public void setUltimos(Ultimos ultimos) {
        this.ultimos = ultimos;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("month", month).append("periods", periods).append("weekDays", weekDays).append("monthDays", monthDays).append("ultimos", ultimos).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(monthDays).append(periods).append(month).append(weekDays).append(ultimos).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Month) == false) {
            return false;
        }
        Month rhs = ((Month) other);
        return new EqualsBuilder().append(monthDays, rhs.monthDays).append(periods, rhs.periods).append(month, rhs.month).append(weekDays, rhs.weekDays).append(ultimos, rhs.ultimos).isEquals();
    }

}
