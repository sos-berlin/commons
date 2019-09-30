
package com.sos.joc.model.joe.order;

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
 * add order in return code
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "return_code_add_order")
@Generated("org.jsonschema2pojo")
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
     * No args constructor for use in serialization
     * 
     */
    public ReturnCodeAddOrder() {
    }

    /**
     * 
     * @param xmlns
     */
    public ReturnCodeAddOrder(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The xmlns
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
     * @param xmlns
     *     The xmlns
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
