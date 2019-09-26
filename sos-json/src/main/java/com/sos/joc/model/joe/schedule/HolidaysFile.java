
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
@JacksonXmlRootElement(localName = "holidays")
@JsonPropertyOrder({
    "weekdays",
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
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekdays", isAttribute = false)
    private Weekdays weekdays;
    @JsonProperty("days")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "holiday", isAttribute = false)
    private List<Holiday> days = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public HolidaysFile() {
    }

    /**
     * 
     * @param weekdays
     * @param days
     */
    public HolidaysFile(Weekdays weekdays, List<Holiday> days) {
        super();
        this.weekdays = weekdays;
        this.days = days;
    }

    /**
     * weekdays
     * <p>
     * 
     * 
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
     */
    @JsonProperty("weekdays")
    @JacksonXmlProperty(localName = "weekdays", isAttribute = false)
    public void setWeekdays(Weekdays weekdays) {
        this.weekdays = weekdays;
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
        return new ToStringBuilder(this).append("weekdays", weekdays).append("days", days).toString();
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
        if ((other instanceof HolidaysFile) == false) {
            return false;
        }
        HolidaysFile rhs = ((HolidaysFile) other);
        return new EqualsBuilder().append(days, rhs.days).append(weekdays, rhs.weekdays).isEquals();
    }

}
