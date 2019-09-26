
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
@JacksonXmlRootElement(localName = "monitor.use")
@JsonPropertyOrder({
    "monitor",
    "ordering"
})
public class MonitorUse {

    /**
     * path to a monitor object
     * (Required)
     * 
     */
    @JsonProperty("monitor")
    @JsonPropertyDescription("path to a monitor object")
    @JacksonXmlProperty(localName = "monitor", isAttribute = true)
    private String monitor;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("ordering")
    @JacksonXmlProperty(localName = "ordering", isAttribute = true)
    private Integer ordering;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MonitorUse() {
    }

    /**
     * 
     * @param ordering
     * @param monitor
     */
    public MonitorUse(String monitor, Integer ordering) {
        super();
        this.monitor = monitor;
        this.ordering = ordering;
    }

    /**
     * path to a monitor object
     * (Required)
     * 
     */
    @JsonProperty("monitor")
    @JacksonXmlProperty(localName = "monitor", isAttribute = true)
    public String getMonitor() {
        return monitor;
    }

    /**
     * path to a monitor object
     * (Required)
     * 
     */
    @JsonProperty("monitor")
    @JacksonXmlProperty(localName = "monitor", isAttribute = true)
    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("ordering")
    @JacksonXmlProperty(localName = "ordering", isAttribute = true)
    public Integer getOrdering() {
        return ordering;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("ordering")
    @JacksonXmlProperty(localName = "ordering", isAttribute = true)
    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("monitor", monitor).append("ordering", ordering).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(monitor).append(ordering).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MonitorUse) == false) {
            return false;
        }
        MonitorUse rhs = ((MonitorUse) other);
        return new EqualsBuilder().append(monitor, rhs.monitor).append(ordering, rhs.ordering).isEquals();
    }

}
