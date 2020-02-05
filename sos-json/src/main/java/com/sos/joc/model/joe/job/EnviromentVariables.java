
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


/**
 * environment
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "environment")
@JsonPropertyOrder({
    "variables"
})
public class EnviromentVariables {

    @JsonProperty("variables")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    private List<EnviromentVariable> variables = null;

    @JsonProperty("variables")
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    public List<EnviromentVariable> getVariables() {
        return variables;
    }

    @JsonProperty("variables")
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    public void setVariables(List<EnviromentVariable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("variables", variables).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(variables).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EnviromentVariables) == false) {
            return false;
        }
        EnviromentVariables rhs = ((EnviromentVariables) other);
        return new EqualsBuilder().append(variables, rhs.variables).isEquals();
    }

}
