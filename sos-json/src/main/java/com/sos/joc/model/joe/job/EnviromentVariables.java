
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
@JacksonXmlRootElement(localName = "environment")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "variables"
})
public class EnviromentVariables {

    @JsonProperty("variables")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    private List<EnviromentVariable> variables = null;

    /**
     * 
     * @return
     *     The variables
     */
    @JsonProperty("variables")
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    public List<EnviromentVariable> getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables
     *     The variables
     */
    @JsonProperty("variables")
    @JacksonXmlProperty(localName = "variable", isAttribute = false)
    public void setVariables(List<EnviromentVariable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
