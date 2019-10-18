
package com.sos.joc.model.xmleditor.read;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor read configuration answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration",
    "schema",
    "state"
})
public class ReadConfigurationAnswer {

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
     * xmleditor read configuration answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     */
    @JsonProperty("state")
    private ReadConfigurationAnswerState state;

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
     * xmleditor read configuration answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public ReadConfigurationAnswerState getState() {
        return state;
    }

    /**
     * xmleditor read configuration answer
     * <p>
     * Describes the situation live/draft
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(ReadConfigurationAnswerState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(configuration).append(schema).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReadConfigurationAnswer) == false) {
            return false;
        }
        ReadConfigurationAnswer rhs = ((ReadConfigurationAnswer) other);
        return new EqualsBuilder().append(configuration, rhs.configuration).append(schema, rhs.schema).append(state, rhs.state).isEquals();
    }

}
