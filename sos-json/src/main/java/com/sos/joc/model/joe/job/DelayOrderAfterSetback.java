
package com.sos.joc.model.joe.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "delay_order_after_setback")
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    private String isMaximum;

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The setbackCount
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
     * @param setbackCount
     *     The setbackCount
     */
    @JsonProperty("setbackCount")
    @JacksonXmlProperty(localName = "setback_count", isAttribute = true)
    public void setSetbackCount(Integer setbackCount) {
        this.setbackCount = setbackCount;
    }

    /**
     * 
     * @return
     *     The delay
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public String getDelay() {
        return delay;
    }

    /**
     * 
     * @param delay
     *     The delay
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(String delay) {
        this.delay = delay;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The isMaximum
     */
    @JsonProperty("isMaximum")
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    public String getIsMaximum() {
        return isMaximum;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param isMaximum
     *     The isMaximum
     */
    @JsonProperty("isMaximum")
    @JacksonXmlProperty(localName = "is_maximum", isAttribute = true)
    public void setIsMaximum(String isMaximum) {
        this.isMaximum = isMaximum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
