
package com.sos.joc.model.joe.jobchain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.order.ReturnCodeAddOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "on_return_code")
@JsonPropertyOrder({
    "returnCode",
    "toState",
    "addOrders"
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
    @JsonProperty("addOrders")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    private List<ReturnCodeAddOrder> addOrders = null;

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

    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public List<ReturnCodeAddOrder> getAddOrders() {
        return addOrders;
    }

    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public void setAddOrders(List<ReturnCodeAddOrder> addOrders) {
        this.addOrders = addOrders;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("returnCode", returnCode).append("toState", toState).append("addOrders", addOrders).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(returnCode).append(addOrders).append(toState).toHashCode();
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
        return new EqualsBuilder().append(returnCode, rhs.returnCode).append(addOrders, rhs.addOrders).append(toState, rhs.toState).isEquals();
    }

}
