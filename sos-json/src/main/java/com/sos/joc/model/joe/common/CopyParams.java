
package com.sos.joc.model.joe.common;

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
@JacksonXmlRootElement(localName = "copy_params")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "from"
})
public class CopyParams {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("from")
    @JacksonXmlProperty(localName = "from", isAttribute = true)
    private String from;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The from
     */
    @JsonProperty("from")
    @JacksonXmlProperty(localName = "from", isAttribute = true)
    public String getFrom() {
        return from;
    }

    /**
     * 
     * (Required)
     * 
     * @param from
     *     The from
     */
    @JsonProperty("from")
    @JacksonXmlProperty(localName = "from", isAttribute = true)
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CopyParams) == false) {
            return false;
        }
        CopyParams rhs = ((CopyParams) other);
        return new EqualsBuilder().append(from, rhs.from).isEquals();
    }

}
