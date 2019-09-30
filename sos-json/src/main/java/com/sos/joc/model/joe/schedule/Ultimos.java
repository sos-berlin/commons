
package com.sos.joc.model.joe.schedule;

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


/**
 * ultimos
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "ultimos")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "days"
})
public class Ultimos {

    /**
     * days
     * <p>
     * 
     * 
     */
    @JsonProperty("days")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    private List<Day> days = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Ultimos() {
    }

    /**
     * 
     * @param days
     */
    public Ultimos(List<Day> days) {
        this.days = days;
    }

    /**
     * days
     * <p>
     * 
     * 
     * @return
     *     The days
     */
    @JsonProperty("days")
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    public List<Day> getDays() {
        return days;
    }

    /**
     * days
     * <p>
     * 
     * 
     * @param days
     *     The days
     */
    @JsonProperty("days")
    @JacksonXmlProperty(localName = "day", isAttribute = false)
    public void setDays(List<Day> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(days).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Ultimos) == false) {
            return false;
        }
        Ultimos rhs = ((Ultimos) other);
        return new EqualsBuilder().append(days, rhs.days).isEquals();
    }

}
