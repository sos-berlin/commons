
package com.sos.joc.model.joe.nodeparams;

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
@JacksonXmlRootElement(localName = "process")
@Generated("org.jsonschema2pojo")
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
        this.state = state;
        this.params = params;
    }

    /**
     * name of the job chain node
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
     * name of the job chain node
     * 
     * @param state
     *     The state
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
     * @return
     *     The params
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
     * @param params
     *     The params
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).append(params).toHashCode();
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
        return new EqualsBuilder().append(state, rhs.state).append(params, rhs.params).isEquals();
    }

}
