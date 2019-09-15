
package com.sos.joc.model.joe.job;

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


/**
 * job monitor
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "monitor")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "ordering",
    "scripts"
})
public class Monitor {

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
    @JsonProperty("scripts")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    private List<Script> scripts = null;

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
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
     * @return
     *     The ordering
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
     * @param ordering
     *     The ordering
     */
    @JsonProperty("ordering")
    @JacksonXmlProperty(localName = "ordering", isAttribute = true)
    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    /**
     * 
     * @return
     *     The scripts
     */
    @JsonProperty("scripts")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public List<Script> getScripts() {
        return scripts;
    }

    /**
     * 
     * @param scripts
     *     The scripts
     */
    @JsonProperty("scripts")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(ordering).append(scripts).toHashCode();
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
        return new EqualsBuilder().append(name, rhs.name).append(ordering, rhs.ordering).append(scripts, rhs.scripts).isEquals();
    }

}
