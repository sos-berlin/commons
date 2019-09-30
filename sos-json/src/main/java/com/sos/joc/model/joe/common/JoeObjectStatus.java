
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
 * filter for requests
 * <p>
 * Describes the situation live/draft
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "joe_object_status")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "message",
    "versionState",
    "deployed"
})
public class JoeObjectStatus {

    /**
     * include
     * <p>
     * 
     * 
     */
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = false)
    private JoeMessage message;
    /**
     * version state text
     * <p>
     * 
     * 
     */
    @JsonProperty("versionState")
    @JacksonXmlProperty(localName = "version_state", isAttribute = false)
    private VersionStateText versionState;
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    private Boolean deployed;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JoeObjectStatus() {
    }

    /**
     * 
     * @param versionState
     * @param deployed
     * @param message
     */
    public JoeObjectStatus(JoeMessage message, VersionStateText versionState, Boolean deployed) {
        this.message = message;
        this.versionState = versionState;
        this.deployed = deployed;
    }

    /**
     * include
     * <p>
     * 
     * 
     * @return
     *     The message
     */
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = false)
    public JoeMessage getMessage() {
        return message;
    }

    /**
     * include
     * <p>
     * 
     * 
     * @param message
     *     The message
     */
    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message", isAttribute = false)
    public void setMessage(JoeMessage message) {
        this.message = message;
    }

    /**
     * version state text
     * <p>
     * 
     * 
     * @return
     *     The versionState
     */
    @JsonProperty("versionState")
    @JacksonXmlProperty(localName = "version_state", isAttribute = false)
    public VersionStateText getVersionState() {
        return versionState;
    }

    /**
     * version state text
     * <p>
     * 
     * 
     * @param versionState
     *     The versionState
     */
    @JsonProperty("versionState")
    @JacksonXmlProperty(localName = "version_state", isAttribute = false)
    public void setVersionState(VersionStateText versionState) {
        this.versionState = versionState;
    }

    /**
     * 
     * @return
     *     The deployed
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * @param deployed
     *     The deployed
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(message).append(versionState).append(deployed).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JoeObjectStatus) == false) {
            return false;
        }
        JoeObjectStatus rhs = ((JoeObjectStatus) other);
        return new EqualsBuilder().append(message, rhs.message).append(versionState, rhs.versionState).append(deployed, rhs.deployed).isEquals();
    }

}
