
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
@JacksonXmlRootElement(localName = "day")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "day",
    "periods"
})
public class Day {

    /**
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?) for weekdays (where sunday is 0 or 7) or 1-31 for monthdays and 0-30 for ultimos
     * (Required)
     * 
     */
    @JsonProperty("day")
    @JacksonXmlProperty(localName = "day", isAttribute = true)
    private String day;
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
    public Day() {
    }

    /**
     * 
     * @param periods
     * @param day
     */
    public Day(String day, List<Period> periods) {
        this.day = day;
        this.periods = periods;
    }

    /**
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?) for weekdays (where sunday is 0 or 7) or 1-31 for monthdays and 0-30 for ultimos
     * (Required)
     * 
     * @return
     *     The day
     */
    @JsonProperty("day")
    @JacksonXmlProperty(localName = "day", isAttribute = true)
    public String getDay() {
        return day;
    }

    /**
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?) for weekdays (where sunday is 0 or 7) or 1-31 for monthdays and 0-30 for ultimos
     * (Required)
     * 
     * @param day
     *     The day
     */
    @JsonProperty("day")
    @JacksonXmlProperty(localName = "day", isAttribute = true)
    public void setDay(String day) {
        this.day = day;
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
        return new HashCodeBuilder().append(day).append(periods).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Day) == false) {
            return false;
        }
        Day rhs = ((Day) other);
        return new EqualsBuilder().append(day, rhs.day).append(periods, rhs.periods).isEquals();
    }

}
