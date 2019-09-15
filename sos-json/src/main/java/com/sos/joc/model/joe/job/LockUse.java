
package com.sos.joc.model.joe.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "lock.use")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "lock",
    "exclusive"
})
public class LockUse {

    /**
     * path to a lock object
     * (Required)
     * 
     */
    @JsonProperty("lock")
    @JacksonXmlProperty(localName = "lock", isAttribute = true)
    private String lock;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("exclusive")
    @JacksonXmlProperty(localName = "exclusive", isAttribute = true)
    private String exclusive;

    /**
     * path to a lock object
     * (Required)
     * 
     * @return
     *     The lock
     */
    @JsonProperty("lock")
    @JacksonXmlProperty(localName = "lock", isAttribute = true)
    public String getLock() {
        return lock;
    }

    /**
     * path to a lock object
     * (Required)
     * 
     * @param lock
     *     The lock
     */
    @JsonProperty("lock")
    @JacksonXmlProperty(localName = "lock", isAttribute = true)
    public void setLock(String lock) {
        this.lock = lock;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The exclusive
     */
    @JsonProperty("exclusive")
    @JacksonXmlProperty(localName = "exclusive", isAttribute = true)
    public String getExclusive() {
        return exclusive;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param exclusive
     *     The exclusive
     */
    @JsonProperty("exclusive")
    @JacksonXmlProperty(localName = "exclusive", isAttribute = true)
    public void setExclusive(String exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lock).append(exclusive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockUse) == false) {
            return false;
        }
        LockUse rhs = ((LockUse) other);
        return new EqualsBuilder().append(lock, rhs.lock).append(exclusive, rhs.exclusive).isEquals();
    }

}
