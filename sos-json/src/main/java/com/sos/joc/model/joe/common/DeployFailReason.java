
package com.sos.joc.model.joe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deploy_fail_reason")
@JsonPropertyOrder({
    "_key",
    "message",
    "owner"
})
public class DeployFailReason {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_key")
    @JacksonXmlProperty(localName = "_key", isAttribute = false)
    private DeployFailReasonType _key;
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    private String message;
    /**
     * useful to substitute in the WRONG_OWNERSHIP message
     * 
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("useful to substitute in the WRONG_OWNERSHIP message")
    @JacksonXmlProperty(localName = "owner", isAttribute = true)
    private String owner;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_key")
    @JacksonXmlProperty(localName = "_key", isAttribute = false)
    public DeployFailReasonType get_key() {
        return _key;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_key")
    @JacksonXmlProperty(localName = "_key", isAttribute = false)
    public void set_key(DeployFailReasonType _key) {
        this._key = _key;
    }

    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * useful to substitute in the WRONG_OWNERSHIP message
     * 
     */
    @JsonProperty("owner")
    @JacksonXmlProperty(localName = "owner", isAttribute = true)
    public String getOwner() {
        return owner;
    }

    /**
     * useful to substitute in the WRONG_OWNERSHIP message
     * 
     */
    @JsonProperty("owner")
    @JacksonXmlProperty(localName = "owner", isAttribute = true)
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("_key", _key).append("message", message).append("owner", owner).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(owner).append(_key).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeployFailReason) == false) {
            return false;
        }
        DeployFailReason rhs = ((DeployFailReason) other);
        return new EqualsBuilder().append(owner, rhs.owner).append(_key, rhs._key).append(message, rhs.message).isEquals();
    }

}
