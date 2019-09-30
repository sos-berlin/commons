
package com.sos.joc.model.joe.jobchain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "to_state")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "state"
})
public class ToState {

    /**
     * name of a job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ToState() {
    }

    /**
     * 
     * @param state
     */
    public ToState(String state) {
        this.state = state;
    }

    /**
     * name of a job chain node
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    /**
     * name of a job chain node
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ToState) == false) {
            return false;
        }
        ToState rhs = ((ToState) other);
        return new EqualsBuilder().append(state, rhs.state).isEquals();
    }

}
