
package com.sos.joc.model.joe.nodeparams;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "order")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobChainNodes"
})
public class ConfigOrder {

    @JsonProperty("jobChainNodes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    private List<ConfigNode> jobChainNodes = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ConfigOrder() {
    }

    /**
     * 
     * @param jobChainNodes
     */
    public ConfigOrder(List<ConfigNode> jobChainNodes) {
        this.jobChainNodes = jobChainNodes;
    }

    /**
     * 
     * @return
     *     The jobChainNodes
     */
    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    public List<ConfigNode> getJobChainNodes() {
        return jobChainNodes;
    }

    /**
     * 
     * @param jobChainNodes
     *     The jobChainNodes
     */
    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    public void setJobChainNodes(List<ConfigNode> jobChainNodes) {
        this.jobChainNodes = jobChainNodes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChainNodes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigOrder) == false) {
            return false;
        }
        ConfigOrder rhs = ((ConfigOrder) other);
        return new EqualsBuilder().append(jobChainNodes, rhs.jobChainNodes).isEquals();
    }

}
