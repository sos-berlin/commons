
package com.sos.joc.model.xmleditor.read.other;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor read OTHER configuration answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration",
    "configurations",
    "schemas"
})
public class ReadOtherConfigurationAnswer {

    /**
     * xmleditor read configuration answer OTHER
     * <p>
     * 
     * 
     */
    @JsonProperty("configuration")
    private AnswerConfiguration configuration;
    @JsonProperty("configurations")
    private Object configurations;
    @JsonProperty("schemas")
    private Object schemas;

    /**
     * xmleditor read configuration answer OTHER
     * <p>
     * 
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    public AnswerConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * xmleditor read configuration answer OTHER
     * <p>
     * 
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    public void setConfiguration(AnswerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 
     * @return
     *     The configurations
     */
    @JsonProperty("configurations")
    public Object getConfigurations() {
        return configurations;
    }

    /**
     * 
     * @param configurations
     *     The configurations
     */
    @JsonProperty("configurations")
    public void setConfigurations(Object configurations) {
        this.configurations = configurations;
    }

    /**
     * 
     * @return
     *     The schemas
     */
    @JsonProperty("schemas")
    public Object getSchemas() {
        return schemas;
    }

    /**
     * 
     * @param schemas
     *     The schemas
     */
    @JsonProperty("schemas")
    public void setSchemas(Object schemas) {
        this.schemas = schemas;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(configuration).append(configurations).append(schemas).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReadOtherConfigurationAnswer) == false) {
            return false;
        }
        ReadOtherConfigurationAnswer rhs = ((ReadOtherConfigurationAnswer) other);
        return new EqualsBuilder().append(configuration, rhs.configuration).append(configurations, rhs.configurations).append(schemas, rhs.schemas).isEquals();
    }

}
