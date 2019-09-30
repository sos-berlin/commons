
package com.sos.joc.model.joe.schedule;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedule
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "schedule")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "validFrom",
    "validTo",
    "substitute",
    "title"
})
public class Schedule
    extends AbstractSchedule
    implements IJSObject
{

    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validFrom")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    private String validFrom;
    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validTo")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    private String validTo;
    /**
     * path to another schedule
     * 
     */
    @JsonProperty("substitute")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    private String substitute;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Schedule() {
    }

    /**
     * 
     * @param validFrom
     * @param title
     * @param substitute
     * @param validTo
     */
    public Schedule(String validFrom, String validTo, String substitute, String title) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.substitute = substitute;
        this.title = title;
    }

    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     * @return
     *     The validFrom
     */
    @JsonProperty("validFrom")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     * @param validFrom
     *     The validFrom
     */
    @JsonProperty("validFrom")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     * @return
     *     The validTo
     */
    @JsonProperty("validTo")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    public String getValidTo() {
        return validTo;
    }

    /**
     * yyyy-mm-dd HH:MM[:SS]
     * 
     * @param validTo
     *     The validTo
     */
    @JsonProperty("validTo")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    /**
     * path to another schedule
     * 
     * @return
     *     The substitute
     */
    @JsonProperty("substitute")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    public String getSubstitute() {
        return substitute;
    }

    /**
     * path to another schedule
     * 
     * @param substitute
     *     The substitute
     */
    @JsonProperty("substitute")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(validFrom).append(validTo).append(substitute).append(title).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Schedule) == false) {
            return false;
        }
        Schedule rhs = ((Schedule) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(validFrom, rhs.validFrom).append(validTo, rhs.validTo).append(substitute, rhs.substitute).append(title, rhs.title).isEquals();
    }

}
