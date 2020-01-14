
package com.sos.joc.model.xmleditor.format;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor xml pretty formatting answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "configuration"
})
public class FormatConfigurationAnswer {

    @JsonProperty("configuration")
    private String configuration;

    @JsonProperty("configuration")
    public String getConfiguration() {
        return configuration;
    }

    @JsonProperty("configuration")
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("configuration", configuration).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(configuration).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FormatConfigurationAnswer) == false) {
            return false;
        }
        FormatConfigurationAnswer rhs = ((FormatConfigurationAnswer) other);
        return new EqualsBuilder().append(configuration, rhs.configuration).isEquals();
    }

}
