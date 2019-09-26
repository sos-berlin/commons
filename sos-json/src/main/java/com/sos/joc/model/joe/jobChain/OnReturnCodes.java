
package com.sos.joc.model.joe.jobchain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "on_return_codes")
@JsonPropertyOrder({
    "onReturnCodeList"
})
public class OnReturnCodes {

    @JsonProperty("onReturnCodeList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "on_return_code", isAttribute = false)
    private List<OnReturnCode> onReturnCodeList = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OnReturnCodes() {
    }

    /**
     * 
     * @param onReturnCodeList
     */
    public OnReturnCodes(List<OnReturnCode> onReturnCodeList) {
        super();
        this.onReturnCodeList = onReturnCodeList;
    }

    @JsonProperty("onReturnCodeList")
    @JacksonXmlProperty(localName = "on_return_code", isAttribute = false)
    public List<OnReturnCode> getOnReturnCodeList() {
        return onReturnCodeList;
    }

    @JsonProperty("onReturnCodeList")
    @JacksonXmlProperty(localName = "on_return_code", isAttribute = false)
    public void setOnReturnCodeList(List<OnReturnCode> onReturnCodeList) {
        this.onReturnCodeList = onReturnCodeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("onReturnCodeList", onReturnCodeList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(onReturnCodeList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OnReturnCodes) == false) {
            return false;
        }
        OnReturnCodes rhs = ((OnReturnCodes) other);
        return new EqualsBuilder().append(onReturnCodeList, rhs.onReturnCodeList).isEquals();
    }

}
