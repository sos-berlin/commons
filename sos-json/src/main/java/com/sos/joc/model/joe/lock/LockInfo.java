
package com.sos.joc.model.joe.lock;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lock info
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "lock_info")
@JsonPropertyOrder({
    "deliveryDate",
    "isLocked",
    "lockedBy",
    "lockedSince"
})
public class LockInfo {

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
    @JsonProperty("isLocked")
    @JacksonXmlProperty(localName = "is_locked", isAttribute = true)
    private Boolean isLocked = false;
    @JsonProperty("lockedBy")
    @JacksonXmlProperty(localName = "locked_by", isAttribute = true)
    private String lockedBy;
    @JsonProperty("lockedSince")
    @JacksonXmlProperty(localName = "locked_since", isAttribute = true)
    private Date lockedSince;

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

    @JsonProperty("isLocked")
    @JacksonXmlProperty(localName = "is_locked", isAttribute = true)
    public Boolean getIsLocked() {
        return isLocked;
    }

    @JsonProperty("isLocked")
    @JacksonXmlProperty(localName = "is_locked", isAttribute = true)
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    @JsonProperty("lockedBy")
    @JacksonXmlProperty(localName = "locked_by", isAttribute = true)
    public String getLockedBy() {
        return lockedBy;
    }

    @JsonProperty("lockedBy")
    @JacksonXmlProperty(localName = "locked_by", isAttribute = true)
    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    @JsonProperty("lockedSince")
    @JacksonXmlProperty(localName = "locked_since", isAttribute = true)
    public Date getLockedSince() {
        return lockedSince;
    }

    @JsonProperty("lockedSince")
    @JacksonXmlProperty(localName = "locked_since", isAttribute = true)
    public void setLockedSince(Date lockedSince) {
        this.lockedSince = lockedSince;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("isLocked", isLocked).append("lockedBy", lockedBy).append("lockedSince", lockedSince).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(lockedBy).append(lockedSince).append(isLocked).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockInfo) == false) {
            return false;
        }
        LockInfo rhs = ((LockInfo) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(lockedBy, rhs.lockedBy).append(lockedSince, rhs.lockedSince).append(isLocked, rhs.isLocked).isEquals();
    }

}
