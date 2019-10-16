
package com.sos.joc.model.joe.lock;

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
 * lock
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "lock")
@JsonPropertyOrder({
    "maxNonExclusive"
})
public class Lock implements IJSObject
{

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    private Integer maxNonExclusive;

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    public Integer getMaxNonExclusive() {
        return maxNonExclusive;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    public void setMaxNonExclusive(Integer maxNonExclusive) {
        this.maxNonExclusive = maxNonExclusive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("maxNonExclusive", maxNonExclusive).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(maxNonExclusive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Lock) == false) {
            return false;
        }
        Lock rhs = ((Lock) other);
        return new EqualsBuilder().append(maxNonExclusive, rhs.maxNonExclusive).isEquals();
    }

}
