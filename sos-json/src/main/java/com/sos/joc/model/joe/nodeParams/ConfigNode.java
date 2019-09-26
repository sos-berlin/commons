
package com.sos.joc.model.joe.nodeparams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "process")
@JsonPropertyOrder({
    "state",
    "params"
})
public class ConfigNode {

    /**
     * name of the job chain node
     * 
     */
    @JsonProperty("state")
    @JsonPropertyDescription("name of the job chain node")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;
    /**
     * job chain node parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private Params params;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ConfigNode() {
    }

    /**
     * 
     * @param state
     * @param params
     */
    public ConfigNode(String state, Params params) {
        super();
        this.state = state;
        this.params = params;
    }

    /**
     * name of the job chain node
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    /**
     * name of the job chain node
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    /**
     * job chain node parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public Params getParams() {
        return params;
    }

    /**
     * job chain node parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("state", state).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(params).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigNode) == false) {
            return false;
        }
        ConfigNode rhs = ((ConfigNode) other);
        return new EqualsBuilder().append(params, rhs.params).append(state, rhs.state).isEquals();
    }

}
