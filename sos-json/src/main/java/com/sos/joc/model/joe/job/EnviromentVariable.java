
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * environment variable
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "variable")
@JsonPropertyOrder({
    "name",
    "value"
})
public class EnviromentVariable {

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    private String value;

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("value", value).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EnviromentVariable) == false) {
            return false;
        }
        EnviromentVariable rhs = ((EnviromentVariable) other);
        return new EqualsBuilder().append(name, rhs.name).append(value, rhs.value).isEquals();
    }

}
