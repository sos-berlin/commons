
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "singleStart",
    "begin",
    "end",
    "letRun",
    "repeat",
    "absoluteRepeat",
    "wheHoliday",
    "once"
})
public class Period {

    @JsonProperty("singleStart")
    private String singleStart;
    @JsonProperty("begin")
    private String begin;
    @JsonProperty("end")
    private String end;
    @JsonProperty("letRun")
    private Boolean letRun;
    @JsonProperty("repeat")
    private String repeat;
    @JsonProperty("absoluteRepeat")
    private String absoluteRepeat;
    @JsonProperty("wheHoliday")
    private String wheHoliday;
    @JsonProperty("once")
    private Boolean once;

    /**
     * 
     * @return
     *     The singleStart
     */
    @JsonProperty("singleStart")
    public String getSingleStart() {
        return singleStart;
    }

    /**
     * 
     * @param singleStart
     *     The singleStart
     */
    @JsonProperty("singleStart")
    public void setSingleStart(String singleStart) {
        this.singleStart = singleStart;
    }

    /**
     * 
     * @return
     *     The begin
     */
    @JsonProperty("begin")
    public String getBegin() {
        return begin;
    }

    /**
     * 
     * @param begin
     *     The begin
     */
    @JsonProperty("begin")
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * 
     * @return
     *     The end
     */
    @JsonProperty("end")
    public String getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    @JsonProperty("end")
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * 
     * @return
     *     The letRun
     */
    @JsonProperty("letRun")
    public Boolean getLetRun() {
        return letRun;
    }

    /**
     * 
     * @param letRun
     *     The letRun
     */
    @JsonProperty("letRun")
    public void setLetRun(Boolean letRun) {
        this.letRun = letRun;
    }

    /**
     * 
     * @return
     *     The repeat
     */
    @JsonProperty("repeat")
    public String getRepeat() {
        return repeat;
    }

    /**
     * 
     * @param repeat
     *     The repeat
     */
    @JsonProperty("repeat")
    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    /**
     * 
     * @return
     *     The absoluteRepeat
     */
    @JsonProperty("absoluteRepeat")
    public String getAbsoluteRepeat() {
        return absoluteRepeat;
    }

    /**
     * 
     * @param absoluteRepeat
     *     The absoluteRepeat
     */
    @JsonProperty("absoluteRepeat")
    public void setAbsoluteRepeat(String absoluteRepeat) {
        this.absoluteRepeat = absoluteRepeat;
    }

    /**
     * 
     * @return
     *     The wheHoliday
     */
    @JsonProperty("wheHoliday")
    public String getWheHoliday() {
        return wheHoliday;
    }

    /**
     * 
     * @param wheHoliday
     *     The wheHoliday
     */
    @JsonProperty("wheHoliday")
    public void setWheHoliday(String wheHoliday) {
        this.wheHoliday = wheHoliday;
    }

    /**
     * 
     * @return
     *     The once
     */
    @JsonProperty("once")
    public Boolean getOnce() {
        return once;
    }

    /**
     * 
     * @param once
     *     The once
     */
    @JsonProperty("once")
    public void setOnce(Boolean once) {
        this.once = once;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(singleStart).append(begin).append(end).append(letRun).append(repeat).append(absoluteRepeat).append(wheHoliday).append(once).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Period) == false) {
            return false;
        }
        Period rhs = ((Period) other);
        return new EqualsBuilder().append(singleStart, rhs.singleStart).append(begin, rhs.begin).append(end, rhs.end).append(letRun, rhs.letRun).append(repeat, rhs.repeat).append(absoluteRepeat, rhs.absoluteRepeat).append(wheHoliday, rhs.wheHoliday).append(once, rhs.once).isEquals();
    }

}
