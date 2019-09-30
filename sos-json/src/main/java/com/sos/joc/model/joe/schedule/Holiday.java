
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "holiday")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "date",
    "calendar",
    "periods"
})
public class Holiday {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    @JacksonXmlProperty(localName = "date", isAttribute = true)
    private String date;
    @JsonProperty("calendar")
    @JacksonXmlProperty(localName = "calendar", isAttribute = true)
    private String calendar;
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
     * No args constructor for use in serialization
     * 
     */
    public Holiday() {
    }

    /**
     * 
     * @param date
     * @param calendar
     * @param periods
     */
    public Holiday(String date, String calendar, List<Period> periods) {
        this.date = date;
        this.calendar = calendar;
        this.periods = periods;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The date
     */
    @JsonProperty("date")
    @JacksonXmlProperty(localName = "date", isAttribute = true)
    public String getDate() {
        return date;
    }

    /**
     * 
     * (Required)
     * 
     * @param date
     *     The date
     */
    @JsonProperty("date")
    @JacksonXmlProperty(localName = "date", isAttribute = true)
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The calendar
     */
    @JsonProperty("calendar")
    @JacksonXmlProperty(localName = "calendar", isAttribute = true)
    public String getCalendar() {
        return calendar;
    }

    /**
     * 
     * @param calendar
     *     The calendar
     */
    @JsonProperty("calendar")
    @JacksonXmlProperty(localName = "calendar", isAttribute = true)
    public void setCalendar(String calendar) {
        this.calendar = calendar;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(date).append(calendar).append(periods).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Holiday) == false) {
            return false;
        }
        Holiday rhs = ((Holiday) other);
        return new EqualsBuilder().append(date, rhs.date).append(calendar, rhs.calendar).append(periods, rhs.periods).isEquals();
    }

}
