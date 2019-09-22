
package com.sos.joc.model.joe.schedule;

import java.util.List;
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
 * holidays
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "holidays")
@JsonPropertyOrder({
    "includes",
    "weekDays",
    "days"
})
public class Holidays {

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    private List<com.sos.joc.model.joe.common.Include> includes = null;
    /**
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekDays")
    @JacksonXmlProperty(localName = "week_days", isAttribute = false)
    private WeekDays weekDays;
    @JsonProperty("days")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "holiday", isAttribute = false)
    private List<Holiday> days = null;

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public List<com.sos.joc.model.joe.common.Include> getIncludes() {
        return includes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setIncludes(List<com.sos.joc.model.joe.common.Include> includes) {
        this.includes = includes;
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

    @JsonProperty("days")
    @JacksonXmlProperty(localName = "holiday", isAttribute = false)
    public List<Holiday> getDays() {
        return days;
    }

    @JsonProperty("days")
    @JacksonXmlProperty(localName = "holiday", isAttribute = false)
    public void setDays(List<Holiday> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("includes", includes).append("weekDays", weekDays).append("days", days).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(days).append(includes).append(weekDays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Holidays) == false) {
            return false;
        }
        Holidays rhs = ((Holidays) other);
        return new EqualsBuilder().append(days, rhs.days).append(includes, rhs.includes).append(weekDays, rhs.weekDays).isEquals();
    }

}
