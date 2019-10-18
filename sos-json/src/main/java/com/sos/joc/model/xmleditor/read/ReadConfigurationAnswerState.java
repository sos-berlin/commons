
package com.sos.joc.model.xmleditor.read;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectVersionState;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor read configuration answer
 * <p>
 * Describes the situation live/draft
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deployed",
    "versionState",
    "message",
    "warning",
    "modified"
})
public class ReadConfigurationAnswerState {

    @JsonProperty("deployed")
    private Boolean deployed;
    /**
     * xmleditor object version state text
     * <p>
     * 
     * 
     */
    @JsonProperty("versionState")
    private ObjectVersionState versionState;
    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     */
    @JsonProperty("message")
    private AnswerMessage message;
    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     */
    @JsonProperty("warning")
    private AnswerMessage warning;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("modified")
    private Date modified;

    /**
     * 
     * @return
     *     The deployed
     */
    @JsonProperty("deployed")
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * @param deployed
     *     The deployed
     */
    @JsonProperty("deployed")
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    /**
     * xmleditor object version state text
     * <p>
     * 
     * 
     * @return
     *     The versionState
     */
    @JsonProperty("versionState")
    public ObjectVersionState getVersionState() {
        return versionState;
    }

    /**
     * xmleditor object version state text
     * <p>
     * 
     * 
     * @param versionState
     *     The versionState
     */
    @JsonProperty("versionState")
    public void setVersionState(ObjectVersionState versionState) {
        this.versionState = versionState;
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

    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     * @return
     *     The warning
     */
    @JsonProperty("warning")
    public AnswerMessage getWarning() {
        return warning;
    }

    /**
     * xmleditor answer message
     * <p>
     * 
     * 
     * @param warning
     *     The warning
     */
    @JsonProperty("warning")
    public void setWarning(AnswerMessage warning) {
        this.warning = warning;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The modified
     */
    @JsonProperty("modified")
    public Date getModified() {
        return modified;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param modified
     *     The modified
     */
    @JsonProperty("modified")
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deployed).append(versionState).append(message).append(warning).append(modified).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReadConfigurationAnswerState) == false) {
            return false;
        }
        ReadConfigurationAnswerState rhs = ((ReadConfigurationAnswerState) other);
        return new EqualsBuilder().append(deployed, rhs.deployed).append(versionState, rhs.versionState).append(message, rhs.message).append(warning, rhs.warning).append(modified, rhs.modified).isEquals();
    }

}
