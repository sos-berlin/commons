
package com.sos.joc.model.joe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * joe object status
 * <p>
 * Describes the situation live/draft
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "joe_object_status")
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
     * include
     * <p>
     * 
     * 
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
     */
    @JsonProperty("versionState")
    @JacksonXmlProperty(localName = "version_state", isAttribute = false)
    public void setVersionState(VersionStateText versionState) {
        this.versionState = versionState;
    }

    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public Boolean getDeployed() {
        return deployed;
    }

    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("message", message).append("versionState", versionState).append("deployed", deployed).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deployed).append(versionState).append(message).toHashCode();
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
        return new EqualsBuilder().append(deployed, rhs.deployed).append(versionState, rhs.versionState).append(message, rhs.message).isEquals();
    }

}
