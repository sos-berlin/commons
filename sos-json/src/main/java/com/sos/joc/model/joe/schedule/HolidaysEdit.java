
package com.sos.joc.model.joe.schedule;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "holidays_edit")
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
     * external holidays
     * <p>
     * 
     * (Required)
     * 
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
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "holidays", isAttribute = false)
    public void setConfiguration(HolidaysFile configuration) {
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
        if ((other instanceof HolidaysEdit) == false) {
            return false;
        }
        HolidaysEdit rhs = ((HolidaysEdit) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(configuration, rhs.configuration).isEquals();
    }

}
