
package com.sos.joc.model.joe.other;

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
        super();
        this.configuration = configuration;
    }

    /**
     * other files
     * <p>
     * 
     * (Required)
     * 
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
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public void setConfiguration(Other configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("configuration", configuration).toString();
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
