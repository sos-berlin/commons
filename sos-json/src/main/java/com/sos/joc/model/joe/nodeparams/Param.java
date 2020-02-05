
package com.sos.joc.model.joe.nodeparams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "param")
@JsonPropertyOrder({
    "name",
    "value",
    "content"
})
public class Param {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    /**
     * parameter value as attribute in xml representation
     * 
     */
    @JsonProperty("value")
    @JsonPropertyDescription("parameter value as attribute in xml representation")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    private String value;
    /**
     * parameter value as cdata in xml representation
     * 
     */
    @JsonProperty("content")
    @JsonPropertyDescription("parameter value as cdata in xml representation")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    private String content;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
    }

    /**
     * parameter value as attribute in xml representation
     * 
     */
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    public String getValue() {
        return value;
    }

    /**
     * parameter value as attribute in xml representation
     * 
     */
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value", isAttribute = true)
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * parameter value as cdata in xml representation
     * 
     */
    @JsonProperty("content")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    public String getContent() {
        return content;
    }

    /**
     * parameter value as cdata in xml representation
     * 
     */
    @JsonProperty("content")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("value", value).append("content", content).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(value).append(content).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Param) == false) {
            return false;
        }
        Param rhs = ((Param) other);
        return new EqualsBuilder().append(name, rhs.name).append(value, rhs.value).append(content, rhs.content).isEquals();
    }

}
