
package com.sos.joc.model.joe.processclass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.JSObjectEdit;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit agent cluster configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "agent_cluster_edit")
@JsonPropertyOrder({
    "configuration"
})
public class AgentClusterEdit
    extends JSObjectEdit
{

    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    private ProcessClass configuration;

    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    public ProcessClass getConfiguration() {
        return configuration;
    }

    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    public void setConfiguration(ProcessClass configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("configuration", configuration).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(configuration).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterEdit) == false) {
            return false;
        }
        AgentClusterEdit rhs = ((AgentClusterEdit) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(configuration, rhs.configuration).isEquals();
    }

}
