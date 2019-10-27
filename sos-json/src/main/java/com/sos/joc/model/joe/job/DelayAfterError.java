
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "delay_after_error")
@JsonPropertyOrder({
    "errorCount",
    "delay"
})
public class DelayAfterError {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorCount")
    @JacksonXmlProperty(localName = "error_count", isAttribute = true)
    private Integer errorCount;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    private String delay;

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorCount")
    @JacksonXmlProperty(localName = "error_count", isAttribute = true)
    public Integer getErrorCount() {
        return errorCount;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorCount")
    @JacksonXmlProperty(localName = "error_count", isAttribute = true)
    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public String getDelay() {
        return delay;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(String delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("errorCount", errorCount).append("delay", delay).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(errorCount).append(delay).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DelayAfterError) == false) {
            return false;
        }
        DelayAfterError rhs = ((DelayAfterError) other);
        return new EqualsBuilder().append(errorCount, rhs.errorCount).append(delay, rhs.delay).isEquals();
    }

}
