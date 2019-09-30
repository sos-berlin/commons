
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
@JacksonXmlRootElement(localName = "month")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "month",
    "periods",
    "weekdays",
    "monthdays",
    "ultimos"
})
public class Month {

    /**
     * unordered space separated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     */
    @JsonProperty("month")
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
     * No args constructor for use in serialization
     * 
     */
    public Month() {
    }

    /**
     * 
     * @param month
     * @param weekdays
     * @param periods
     * @param monthdays
     * @param ultimos
     */
    public Month(String month, List<Period> periods, Weekdays weekdays, Monthdays monthdays, Ultimos ultimos) {
        this.month = month;
        this.periods = periods;
        this.weekdays = weekdays;
        this.monthdays = monthdays;
        this.ultimos = ultimos;
    }

    /**
     * unordered space separated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     * @return
     *     The month
     */
    @JsonProperty("month")
    @JacksonXmlProperty(localName = "month", isAttribute = true)
    public String getMonth() {
        return month;
    }

    /**
     * unordered space separated list of 1-12 or january, february, march, april, may, june, july, august, september, october, november, december
     * 
     * @param month
     *     The month
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(month).append(periods).append(weekdays).append(monthdays).append(ultimos).toHashCode();
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
        return new EqualsBuilder().append(month, rhs.month).append(periods, rhs.periods).append(weekdays, rhs.weekdays).append(monthdays, rhs.monthdays).append(ultimos, rhs.ultimos).isEquals();
    }

}
