
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


/**
 * Deploy message
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "deploy_message")
@JsonPropertyOrder({
    "permissionDeniedFor",
    "wrongObjectType",
    "message"
})
public class DeployMessage {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("permissionDeniedFor")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "permission_denied_for", isAttribute = true)
    private String permissionDeniedFor;
    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    private String wrongObjectType;
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    private String message;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("permissionDeniedFor")
    @JacksonXmlProperty(localName = "permission_denied_for", isAttribute = true)
    public String getPermissionDeniedFor() {
        return permissionDeniedFor;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("permissionDeniedFor")
    @JacksonXmlProperty(localName = "permission_denied_for", isAttribute = true)
    public void setPermissionDeniedFor(String permissionDeniedFor) {
        this.permissionDeniedFor = permissionDeniedFor;
    }

    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    public String getWrongObjectType() {
        return wrongObjectType;
    }

    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    public void setWrongObjectType(String wrongObjectType) {
        this.wrongObjectType = wrongObjectType;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("permissionDeniedFor", permissionDeniedFor).append("wrongObjectType", wrongObjectType).append("message", message).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(permissionDeniedFor).append(message).append(wrongObjectType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeployMessage) == false) {
            return false;
        }
        DeployMessage rhs = ((DeployMessage) other);
        return new EqualsBuilder().append(permissionDeniedFor, rhs.permissionDeniedFor).append(message, rhs.message).append(wrongObjectType, rhs.wrongObjectType).isEquals();
    }

}
