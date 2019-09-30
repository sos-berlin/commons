
package com.sos.joc.model.joe.schedule;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * period
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "period")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "begin",
    "end",
    "singleStart",
    "letRun",
    "runOnce",
    "repeat",
    "absoluteRepeat",
    "whenHoliday"
})
public class Period {

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    private String begin;
    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    private String end;
    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     */
    @JsonProperty("singleStart")
    @JacksonXmlProperty(localName = "single_start", isAttribute = true)
    private String singleStart;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    private String letRun;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "start_once", isAttribute = true)
    private String runOnce;
    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    private String repeat;
    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     */
    @JsonProperty("absoluteRepeat")
    @JacksonXmlProperty(localName = "absolute_repeat", isAttribute = true)
    private String absoluteRepeat;
    /**
     * possible values: suppress (default), ignore_holiday, previous_non_holiday, next_non_holiday
     * 
     */
    @JsonProperty("whenHoliday")
    @JacksonXmlProperty(localName = "when_holiday", isAttribute = true)
    private String whenHoliday;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Period() {
    }

    /**
     * 
     * @param letRun
     * @param singleStart
     * @param repeat
     * @param end
     * @param absoluteRepeat
     * @param begin
     * @param runOnce
     * @param whenHoliday
     */
    public Period(String begin, String end, String singleStart, String letRun, String runOnce, String repeat, String absoluteRepeat, String whenHoliday) {
        this.begin = begin;
        this.end = end;
        this.singleStart = singleStart;
        this.letRun = letRun;
        this.runOnce = runOnce;
        this.repeat = repeat;
        this.absoluteRepeat = absoluteRepeat;
        this.whenHoliday = whenHoliday;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @return
     *     The begin
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public String getBegin() {
        return begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @param begin
     *     The begin
     */
    @JsonProperty("begin")
    @JacksonXmlProperty(localName = "begin", isAttribute = true)
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @return
     *     The end
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public String getEnd() {
        return end;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @param end
     *     The end
     */
    @JsonProperty("end")
    @JacksonXmlProperty(localName = "end", isAttribute = true)
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @return
     *     The singleStart
     */
    @JsonProperty("singleStart")
    @JacksonXmlProperty(localName = "single_start", isAttribute = true)
    public String getSingleStart() {
        return singleStart;
    }

    /**
     * pattern: [0-9]{1,2}:[0-9]{2}(:[0-9]{2})?
     * 
     * @param singleStart
     *     The singleStart
     */
    @JsonProperty("singleStart")
    @JacksonXmlProperty(localName = "single_start", isAttribute = true)
    public void setSingleStart(String singleStart) {
        this.singleStart = singleStart;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The letRun
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public String getLetRun() {
        return letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param letRun
     *     The letRun
     */
    @JsonProperty("letRun")
    @JacksonXmlProperty(localName = "let_run", isAttribute = true)
    public void setLetRun(String letRun) {
        this.letRun = letRun;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The runOnce
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "start_once", isAttribute = true)
    public String getRunOnce() {
        return runOnce;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param runOnce
     *     The runOnce
     */
    @JsonProperty("runOnce")
    @JacksonXmlProperty(localName = "start_once", isAttribute = true)
    public void setRunOnce(String runOnce) {
        this.runOnce = runOnce;
    }

    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     * @return
     *     The repeat
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    public String getRepeat() {
        return repeat;
    }

    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     * @param repeat
     *     The repeat
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     * @return
     *     The absoluteRepeat
     */
    @JsonProperty("absoluteRepeat")
    @JacksonXmlProperty(localName = "absolute_repeat", isAttribute = true)
    public String getAbsoluteRepeat() {
        return absoluteRepeat;
    }

    /**
     * pattern: ([0-9]+)|([0-9]+:[0-9]{2}(:[0-9]{2})?)
     * 
     * @param absoluteRepeat
     *     The absoluteRepeat
     */
    @JsonProperty("absoluteRepeat")
    @JacksonXmlProperty(localName = "absolute_repeat", isAttribute = true)
    public void setAbsoluteRepeat(String absoluteRepeat) {
        this.absoluteRepeat = absoluteRepeat;
    }

    /**
     * possible values: suppress (default), ignore_holiday, previous_non_holiday, next_non_holiday
     * 
     * @return
     *     The whenHoliday
     */
    @JsonProperty("whenHoliday")
    @JacksonXmlProperty(localName = "when_holiday", isAttribute = true)
    public String getWhenHoliday() {
        return whenHoliday;
    }

    /**
     * possible values: suppress (default), ignore_holiday, previous_non_holiday, next_non_holiday
     * 
     * @param whenHoliday
     *     The whenHoliday
     */
    @JsonProperty("whenHoliday")
    @JacksonXmlProperty(localName = "when_holiday", isAttribute = true)
    public void setWhenHoliday(String whenHoliday) {
        this.whenHoliday = whenHoliday;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(begin).append(end).append(singleStart).append(letRun).append(runOnce).append(repeat).append(absoluteRepeat).append(whenHoliday).toHashCode();
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
        return new EqualsBuilder().append(begin, rhs.begin).append(end, rhs.end).append(singleStart, rhs.singleStart).append(letRun, rhs.letRun).append(runOnce, rhs.runOnce).append(repeat, rhs.repeat).append(absoluteRepeat, rhs.absoluteRepeat).append(whenHoliday, rhs.whenHoliday).isEquals();
    }

}
