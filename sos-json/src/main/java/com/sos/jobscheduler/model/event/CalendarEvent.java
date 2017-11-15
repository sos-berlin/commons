
package com.sos.jobscheduler.model.event;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler custom event for calendar and calendar usage
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "key",
    "variables"
})
public class CalendarEvent {

    @JsonProperty("key")
    private String key;
    @JsonProperty("variables")
    private CalendarVariables variables;

    /**
     * 
     * @return
     *     The key
     */
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The variables
     */
    @JsonProperty("variables")
    public CalendarVariables getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables
     *     The variables
     */
    @JsonProperty("variables")
    public void setVariables(CalendarVariables variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(key).append(variables).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CalendarEvent) == false) {
            return false;
        }
        CalendarEvent rhs = ((CalendarEvent) other);
        return new EqualsBuilder().append(key, rhs.key).append(variables, rhs.variables).isEquals();
    }

}
