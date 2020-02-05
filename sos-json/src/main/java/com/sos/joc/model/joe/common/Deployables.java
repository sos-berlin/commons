
package com.sos.joc.model.joe.common;

import java.util.Date;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * List of deployables
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deployables")
@JsonPropertyOrder({
    "deliveryDate",
    "deployables"
})
public class Deployables {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    @JsonProperty("deployables")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    private Set<Deployable> deployables = null;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @JsonProperty("deployables")
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    public Set<Deployable> getDeployables() {
        return deployables;
    }

    @JsonProperty("deployables")
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    public void setDeployables(Set<Deployable> deployables) {
        this.deployables = deployables;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("deployables", deployables).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(deployables).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Deployables) == false) {
            return false;
        }
        Deployables rhs = ((Deployables) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(deployables, rhs.deployables).isEquals();
    }

}
