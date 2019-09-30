
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "description")
@Generated("org.jsonschema2pojo")
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
        this.inculdes = inculdes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     * @return
     *     The inculdes
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
     * @param inculdes
     *     The inculdes
     */
    @JsonProperty("inculdes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setInculdes(List<com.sos.joc.model.joe.common.Include> inculdes) {
        this.inculdes = inculdes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
