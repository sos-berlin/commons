
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
@JacksonXmlRootElement(localName = "weekday")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "day",
    "which",
    "periods"
})
public class WeekdayOfMonth {

    /**
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?)
     * (Required)
     * 
     */
    @JsonProperty("day")
    @JacksonXmlProperty(localName = "day", isAttribute = true)
    private String day;
    /**
     * possible value: -4, -3, -2, -1, 1, 2, 3, 4
     * (Required)
     * 
     */
    @JsonProperty("which")
    @JacksonXmlProperty(localName = "which", isAttribute = true)
    private Integer which;
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
    public WeekdayOfMonth() {
    }

    /**
     * 
     * @param which
     * @param periods
     * @param day
     */
    public WeekdayOfMonth(String day, Integer which, List<Period> periods) {
        this.day = day;
        this.which = which;
        this.periods = periods;
    }

    /**
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?)
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
     * [01234567]|(so(nntag)?)|(mo(ntag)?)|(di(enstag)?)|(mi(ttwoch)?)|(do(nnerstag)?)|(fr(eitag)?)|(sa(mstag)?)|(sun(day)?)|(mon(day)?)|(tue(sday)?)|(wed(nesday)?)|(thu(rsday)?)|(fri(day)?)|(sat(urday)?)
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
     * possible value: -4, -3, -2, -1, 1, 2, 3, 4
     * (Required)
     * 
     * @return
     *     The which
     */
    @JsonProperty("which")
    @JacksonXmlProperty(localName = "which", isAttribute = true)
    public Integer getWhich() {
        return which;
    }

    /**
     * possible value: -4, -3, -2, -1, 1, 2, 3, 4
     * (Required)
     * 
     * @param which
     *     The which
     */
    @JsonProperty("which")
    @JacksonXmlProperty(localName = "which", isAttribute = true)
    public void setWhich(Integer which) {
        this.which = which;
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
        return new HashCodeBuilder().append(day).append(which).append(periods).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WeekdayOfMonth) == false) {
            return false;
        }
        WeekdayOfMonth rhs = ((WeekdayOfMonth) other);
        return new EqualsBuilder().append(day, rhs.day).append(which, rhs.which).append(periods, rhs.periods).isEquals();
    }

}
