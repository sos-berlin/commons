
package com.sos.joc.model.xmleditor.deploy;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor deploy configuration answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deployed",
    "message"
})
public class DeployConfigurationAnswer {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("deployed")
    private Date deployed;
    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     */
    @JsonProperty("message")
    private AnswerMessage message;

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The deployed
     */
    @JsonProperty("deployed")
    public Date getDeployed() {
        return deployed;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param deployed
     *     The deployed
     */
    @JsonProperty("deployed")
    public void setDeployed(Date deployed) {
        this.deployed = deployed;
    }

    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     * @return
     *     The message
     */
    @JsonProperty("message")
    public AnswerMessage getMessage() {
        return message;
    }

    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     * @param message
     *     The message
     */
    @JsonProperty("message")
    public void setMessage(AnswerMessage message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deployed).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeployConfigurationAnswer) == false) {
            return false;
        }
        DeployConfigurationAnswer rhs = ((DeployConfigurationAnswer) other);
        return new EqualsBuilder().append(deployed, rhs.deployed).append(message, rhs.message).isEquals();
    }

}
