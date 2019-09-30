
package com.sos.joc.model.joe.schedule;

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
 * edit holidays configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "holidays_edit")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration"
})
public class HolidaysEdit
    extends JSObjectEdit
{

    /**
     * external holidays
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    private HolidaysFile configuration;

    /**
     * No args constructor for use in serialization
     * 
     */
    public HolidaysEdit() {
    }

    /**
     * 
     * @param configuration
     */
    public HolidaysEdit(HolidaysFile configuration) {
        this.configuration = configuration;
    }

    /**
     * external holidays
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public HolidaysFile getConfiguration() {
        return configuration;
    }

    /**
     * external holidays
     * <p>
     * 
     * (Required)
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public void setConfiguration(HolidaysFile configuration) {
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
        if ((other instanceof HolidaysEdit) == false) {
            return false;
        }
        HolidaysEdit rhs = ((HolidaysEdit) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(configuration, rhs.configuration).isEquals();
    }

}
