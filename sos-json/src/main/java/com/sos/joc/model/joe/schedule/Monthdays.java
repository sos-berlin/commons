
package com.sos.joc.model.joe.schedule;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * monthdays
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "monthdays")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "days",
    "weekdays"
})
public class Monthdays {

    /**
     * days
     * <p>
     * 
     * 
     */
    @JsonProperty("days")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    private List<Day> days = null;
    @JsonProperty("weekdays")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "weekday", isAttribute = false)
    private List<WeekdayOfMonth> weekdays = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Monthdays() {
    }

    /**
     * 
     * @param weekdays
     * @param days
     */
    public Monthdays(List<Day> days, List<WeekdayOfMonth> weekdays) {
        this.days = days;
        this.weekdays = weekdays;
    }

    /**
     * days
     * <p>
     * 
     * 
     * @return
     *     The days
     */
    @JsonProperty("days")
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    public List<Day> getDays() {
        return days;
    }

    /**
     * days
     * <p>
     * 
     * 
     * @param days
     *     The days
     */
    @JsonProperty("days")
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    public void setDays(List<Day> days) {
        this.days = days;
    }

    /**
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekday", isAttribute = false)
    public List<WeekdayOfMonth> getWeekdays() {
        return weekdays;
    }

    /**
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekday", isAttribute = false)
    public void setWeekdays(List<WeekdayOfMonth> weekdays) {
        this.weekdays = weekdays;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(days).append(weekdays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Monthdays) == false) {
            return false;
        }
        Monthdays rhs = ((Monthdays) other);
        return new EqualsBuilder().append(days, rhs.days).append(weekdays, rhs.weekdays).isEquals();
    }

}
