
package com.sos.joc.model.joe.nodeparams;

import java.util.List;
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
@JsonPropertyOrder({
    "jobChainNodes"
})
public class ConfigOrder {

    @JsonProperty("jobChainNodes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    private List<ConfigNode> jobChainNodes = null;

    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    public List<ConfigNode> getJobChainNodes() {
        return jobChainNodes;
    }

    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "process", isAttribute = false)
    public void setJobChainNodes(List<ConfigNode> jobChainNodes) {
        this.jobChainNodes = jobChainNodes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobChainNodes", jobChainNodes).toString();
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
