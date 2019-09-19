
package com.sos.joc.model.joe.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * add order in return code
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "return_code_add_order")
@JsonPropertyOrder({
    "xmlns"
})
public class ReturnCodeAddOrder
    extends AddOrder
{

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    private String xmlns = "https://jobscheduler-plugins.sos-berlin.com/NodeOrderPlugin";

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    public String getXmlns() {
        return xmlns;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("xmlns", xmlns).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(xmlns).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReturnCodeAddOrder) == false) {
            return false;
        }
        ReturnCodeAddOrder rhs = ((ReturnCodeAddOrder) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(xmlns, rhs.xmlns).isEquals();
    }

}
