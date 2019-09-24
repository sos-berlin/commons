
package com.sos.joc.model.joe.jobchain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.order.ReturnCodeAddOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "on_return_code")
@JsonPropertyOrder({
    "returnCode",
    "toState",
    "addOrder"
})
public class OnReturnCode {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("returnCode")
    @JacksonXmlProperty(localName = "return_code", isAttribute = true)
    private String returnCode;
    @JsonProperty("toState")
    @JacksonXmlProperty(localName = "to_state", isAttribute = false)
    private ToState toState;
    /**
     * add order in return code
     * <p>
     * 
     * 
     */
    @JsonProperty("addOrder")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    private ReturnCodeAddOrder addOrder;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("returnCode")
    @JacksonXmlProperty(localName = "return_code", isAttribute = true)
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("returnCode")
    @JacksonXmlProperty(localName = "return_code", isAttribute = true)
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @JsonProperty("toState")
    @JacksonXmlProperty(localName = "to_state", isAttribute = false)
    public ToState getToState() {
        return toState;
    }

    @JsonProperty("toState")
    @JacksonXmlProperty(localName = "to_state", isAttribute = false)
    public void setToState(ToState toState) {
        this.toState = toState;
    }

    /**
     * add order in return code
     * <p>
     * 
     * 
     */
    @JsonProperty("addOrder")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public ReturnCodeAddOrder getAddOrder() {
        return addOrder;
    }

    /**
     * add order in return code
     * <p>
     * 
     * 
     */
    @JsonProperty("addOrder")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public void setAddOrder(ReturnCodeAddOrder addOrder) {
        this.addOrder = addOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("returnCode", returnCode).append("toState", toState).append("addOrder", addOrder).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(returnCode).append(addOrder).append(toState).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OnReturnCode) == false) {
            return false;
        }
        OnReturnCode rhs = ((OnReturnCode) other);
        return new EqualsBuilder().append(returnCode, rhs.returnCode).append(addOrder, rhs.addOrder).append(toState, rhs.toState).isEquals();
    }

}
