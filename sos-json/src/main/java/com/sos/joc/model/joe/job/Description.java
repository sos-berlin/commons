
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

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "description")
@JsonPropertyOrder({
    "includes"
})
public class Description {

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    private List<com.sos.joc.model.joe.common.Include> includes = null;

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public List<com.sos.joc.model.joe.common.Include> getIncludes() {
        return includes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setIncludes(List<com.sos.joc.model.joe.common.Include> includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("includes", includes).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(includes).toHashCode();
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
        return new EqualsBuilder().append(includes, rhs.includes).isEquals();
    }

}
