
package com.sos.joc.model.joe.other;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.JSObjectEdit;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit other files
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "other_edit")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration"
})
public class OtherEdit
    extends JSObjectEdit
{

    /**
     * other files
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    private Other configuration;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OtherEdit() {
    }

    /**
     * 
     * @param configuration
     */
    public OtherEdit(Other configuration) {
        this.configuration = configuration;
    }

    /**
     * other files
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public Other getConfiguration() {
        return configuration;
    }

    /**
     * other files
     * <p>
     * 
     * (Required)
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public void setConfiguration(Other configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(configuration).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OtherEdit) == false) {
            return false;
        }
        OtherEdit rhs = ((OtherEdit) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(configuration, rhs.configuration).isEquals();
    }

}
