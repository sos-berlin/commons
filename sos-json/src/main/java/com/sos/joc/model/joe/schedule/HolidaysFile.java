
package com.sos.joc.model.joe.schedule;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * external holidays
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "holidays_file")
@JsonPropertyOrder({
    "weekDays",
    "days"
})
public class HolidaysFile implements IJSObject
{

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
        return new ToStringBuilder(this).append("weekDays", weekDays).append("days", days).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(weekDays).append(days).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HolidaysFile) == false) {
            return false;
        }
        HolidaysFile rhs = ((HolidaysFile) other);
        return new EqualsBuilder().append(weekDays, rhs.weekDays).append(days, rhs.days).isEquals();
    }

}
