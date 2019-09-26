
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "delay_order_after_setback")
@JsonPropertyOrder({
    "setbackCount",
    "delay",
    "isMaximum"
})
public class DelayOrderAfterSetback {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("setbackCount")
    @JacksonXmlProperty(localName = "setback_count", isAttribute = true)
    private Integer setbackCount;
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    private String delay;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("isMaximum")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    private String isMaximum;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DelayOrderAfterSetback() {
    }

    /**
     * 
     * @param setbackCount
     * @param delay
     * @param isMaximum
     */
    public DelayOrderAfterSetback(Integer setbackCount, String delay, String isMaximum) {
        super();
        this.setbackCount = setbackCount;
        this.delay = delay;
        this.isMaximum = isMaximum;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("setbackCount")
    @JacksonXmlProperty(localName = "setback_count", isAttribute = true)
    public Integer getSetbackCount() {
        return setbackCount;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("setbackCount")
    @JacksonXmlProperty(localName = "setback_count", isAttribute = true)
    public void setSetbackCount(Integer setbackCount) {
        this.setbackCount = setbackCount;
    }

    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public String getDelay() {
        return delay;
    }

    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(String delay) {
        this.delay = delay;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("isMaximum")
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    public String getIsMaximum() {
        return isMaximum;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("isMaximum")
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    public void setIsMaximum(String isMaximum) {
        this.isMaximum = isMaximum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("setbackCount", setbackCount).append("delay", delay).append("isMaximum", isMaximum).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(setbackCount).append(delay).append(isMaximum).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DelayOrderAfterSetback) == false) {
            return false;
        }
        DelayOrderAfterSetback rhs = ((DelayOrderAfterSetback) other);
        return new EqualsBuilder().append(setbackCount, rhs.setbackCount).append(delay, rhs.delay).append(isMaximum, rhs.isMaximum).isEquals();
    }

}
