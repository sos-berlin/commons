
package com.sos.joc.model.joe.job;

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
@JacksonXmlRootElement(localName = "description")
@JsonPropertyOrder({
    "inculdes"
})
public class Description {

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("inculdes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    private List<com.sos.joc.model.joe.common.Include> inculdes = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Description() {
    }

    /**
     * 
     * @param inculdes
     */
    public Description(List<com.sos.joc.model.joe.common.Include> inculdes) {
        super();
        this.inculdes = inculdes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("inculdes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public List<com.sos.joc.model.joe.common.Include> getInculdes() {
        return inculdes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("inculdes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setInculdes(List<com.sos.joc.model.joe.common.Include> inculdes) {
        this.inculdes = inculdes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("inculdes", inculdes).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(inculdes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Description) == false) {
            return false;
        }
        Description rhs = ((Description) other);
        return new EqualsBuilder().append(inculdes, rhs.inculdes).isEquals();
    }

}
