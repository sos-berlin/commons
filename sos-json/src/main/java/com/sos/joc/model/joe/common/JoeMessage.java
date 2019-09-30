
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
 * include
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "joe_message")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "messageText",
    "_messageCode"
})
public class JoeMessage {

    @JsonProperty("messageText")
    @JacksonXmlProperty(localName = "message_text", isAttribute = true)
    private String messageText;
    @JsonProperty("_messageCode")
    @JacksonXmlProperty(localName = "_message_code", isAttribute = true)
    private String _messageCode;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JoeMessage() {
    }

    /**
     * 
     * @param messageText
     * @param _messageCode
     */
    public JoeMessage(String messageText, String _messageCode) {
        this.messageText = messageText;
        this._messageCode = _messageCode;
    }

    /**
     * 
     * @return
     *     The messageText
     */
    @JsonProperty("messageText")
    @JacksonXmlProperty(localName = "message_text", isAttribute = true)
    public String getMessageText() {
        return messageText;
    }

    /**
     * 
     * @param messageText
     *     The messageText
     */
    @JsonProperty("messageText")
    @JacksonXmlProperty(localName = "message_text", isAttribute = true)
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * 
     * @return
     *     The _messageCode
     */
    @JsonProperty("_messageCode")
    @JacksonXmlProperty(localName = "_message_code", isAttribute = true)
    public String get_messageCode() {
        return _messageCode;
    }

    /**
     * 
     * @param _messageCode
     *     The _messageCode
     */
    @JsonProperty("_messageCode")
    @JacksonXmlProperty(localName = "_message_code", isAttribute = true)
    public void set_messageCode(String _messageCode) {
        this._messageCode = _messageCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(messageText).append(_messageCode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JoeMessage) == false) {
            return false;
        }
        JoeMessage rhs = ((JoeMessage) other);
        return new EqualsBuilder().append(messageText, rhs.messageText).append(_messageCode, rhs._messageCode).isEquals();
    }

}
