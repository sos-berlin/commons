
package com.sos.joc.model.joe.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "deploy_message")
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "permission_denied_for", isAttribute = true)
    private String permissionDeniedFor;
    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    private String wrongObjectType;
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    private String message;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DeployMessage() {
    }

    /**
     * 
     * @param wrongObjectType
     * @param permissionDeniedFor
     * @param message
     */
    public DeployMessage(String permissionDeniedFor, String wrongObjectType, String message) {
        this.permissionDeniedFor = permissionDeniedFor;
        this.wrongObjectType = wrongObjectType;
        this.message = message;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The permissionDeniedFor
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
     * @param permissionDeniedFor
     *     The permissionDeniedFor
     */
    @JsonProperty("permissionDeniedFor")
    @JacksonXmlProperty(localName = "permission_denied_for", isAttribute = true)
    public void setPermissionDeniedFor(String permissionDeniedFor) {
        this.permissionDeniedFor = permissionDeniedFor;
    }

    /**
     * 
     * @return
     *     The wrongObjectType
     */
    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    public String getWrongObjectType() {
        return wrongObjectType;
    }

    /**
     * 
     * @param wrongObjectType
     *     The wrongObjectType
     */
    @JsonProperty("wrongObjectType")
    @JacksonXmlProperty(localName = "wrong_object_type", isAttribute = true)
    public void setWrongObjectType(String wrongObjectType) {
        this.wrongObjectType = wrongObjectType;
    }

    /**
     * 
     * @return
     *     The message
     */
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = true)
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(permissionDeniedFor).append(wrongObjectType).append(message).toHashCode();
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
        return new EqualsBuilder().append(permissionDeniedFor, rhs.permissionDeniedFor).append(wrongObjectType, rhs.wrongObjectType).append(message, rhs.message).isEquals();
    }

}
