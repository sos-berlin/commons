
package com.sos.joc.model.xmleditor.read.other;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor read configuration answer OTHER
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "exists",
    "name",
    "schema",
    "configuration",
    "modified"
})
public class AnswerConfiguration {

    @JsonProperty("exists")
    private Boolean exists;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("schema")
    private String schema;
    @JsonProperty("configuration")
    private String configuration;
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
     *     The exists
     */
    @JsonProperty("exists")
    public Boolean getExists() {
        return exists;
    }

    /**
     * 
     * @param exists
     *     The exists
     */
    @JsonProperty("exists")
    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
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
     * @param schema
     *     The schema
     */
    @JsonProperty("schema")
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
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
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
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
        return new HashCodeBuilder().append(exists).append(name).append(schema).append(configuration).append(modified).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AnswerConfiguration) == false) {
            return false;
        }
        AnswerConfiguration rhs = ((AnswerConfiguration) other);
        return new EqualsBuilder().append(exists, rhs.exists).append(name, rhs.name).append(schema, rhs.schema).append(configuration, rhs.configuration).append(modified, rhs.modified).isEquals();
    }

}
