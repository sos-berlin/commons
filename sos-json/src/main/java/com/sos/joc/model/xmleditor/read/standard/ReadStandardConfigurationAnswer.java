
package com.sos.joc.model.xmleditor.read.standard;

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
 * xmleditor read standard configuration (YADE, NOTIFICATION) answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration",
    "schema",
    "state",
    "warning",
    "modified"
})
public class ReadStandardConfigurationAnswer {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    private String configuration;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("schema")
    private String schema;
    /**
     * xmleditor read standard configuration (YADE, NOTIFICATION) answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     */
    @JsonProperty("state")
    private ReadStandardConfigurationAnswerState state;
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
     * (Required)
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    public String getConfiguration() {
        return configuration;
    }

    /**
     * 
     * (Required)
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The schema
     */
    @JsonProperty("schema")
    public String getSchema() {
        return schema;
    }

    /**
     * 
     * (Required)
     * 
     * @param schema
     *     The schema
     */
    @JsonProperty("schema")
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * xmleditor read standard configuration (YADE, NOTIFICATION) answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public ReadStandardConfigurationAnswerState getState() {
        return state;
    }

    /**
     * xmleditor read standard configuration (YADE, NOTIFICATION) answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(ReadStandardConfigurationAnswerState state) {
        this.state = state;
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
        return new HashCodeBuilder().append(configuration).append(schema).append(state).append(warning).append(modified).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReadStandardConfigurationAnswer) == false) {
            return false;
        }
        ReadStandardConfigurationAnswer rhs = ((ReadStandardConfigurationAnswer) other);
        return new EqualsBuilder().append(configuration, rhs.configuration).append(schema, rhs.schema).append(state, rhs.state).append(warning, rhs.warning).append(modified, rhs.modified).isEquals();
    }

}
