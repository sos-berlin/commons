
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job monitor
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "monitor")
@JsonPropertyOrder({
    "name",
    "ordering",
    "script"
})
public class Monitor implements IJSObject
{

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
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
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    private Script script;

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
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

    /**
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public Script getScript() {
        return script;
    }

    /**
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public void setScript(Script script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("ordering", ordering).append("script", script).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(ordering).append(script).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Monitor) == false) {
            return false;
        }
        Monitor rhs = ((Monitor) other);
        return new EqualsBuilder().append(name, rhs.name).append(ordering, rhs.ordering).append(script, rhs.script).isEquals();
    }

}
