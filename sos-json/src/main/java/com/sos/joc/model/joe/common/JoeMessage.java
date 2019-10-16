
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
 * include
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "joe_message")
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

    @JsonProperty("messageText")
    @JacksonXmlProperty(localName = "message_text", isAttribute = true)
    public String getMessageText() {
        return messageText;
    }

    @JsonProperty("messageText")
    @JacksonXmlProperty(localName = "message_text", isAttribute = true)
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @JsonProperty("_messageCode")
    @JacksonXmlProperty(localName = "_message_code", isAttribute = true)
    public String get_messageCode() {
        return _messageCode;
    }

    @JsonProperty("_messageCode")
    @JacksonXmlProperty(localName = "_message_code", isAttribute = true)
    public void set_messageCode(String _messageCode) {
        this._messageCode = _messageCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("messageText", messageText).append("_messageCode", _messageCode).toString();
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
