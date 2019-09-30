
package com.sos.joc.model.joe.nodeparams;

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
@JacksonXmlRootElement(localName = "job_chain")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "order"
})
public class ConfigJobChain {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = false)
    private ConfigOrder order;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ConfigJobChain() {
    }

    /**
     * 
     * @param name
     * @param order
     */
    public ConfigJobChain(String name, ConfigOrder order) {
        this.name = name;
        this.order = order;
    }

    /**
     * 
     * (Required)
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
     * (Required)
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
     * 
     * (Required)
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = false)
    public ConfigOrder getOrder() {
        return order;
    }

    /**
     * 
     * (Required)
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = false)
    public void setOrder(ConfigOrder order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(order).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigJobChain) == false) {
            return false;
        }
        ConfigJobChain rhs = ((ConfigJobChain) other);
        return new EqualsBuilder().append(name, rhs.name).append(order, rhs.order).isEquals();
    }

}
