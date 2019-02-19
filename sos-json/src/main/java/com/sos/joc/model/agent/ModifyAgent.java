
package com.sos.joc.model.agent;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent modify
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "modify_hot_folder")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "folder",
    "processClass"
})
public class ModifyAgent {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    private String folder;
    /**
     * agent configuration
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private AgentConfiguration processClass;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The folder
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public String getFolder() {
        return folder;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param folder
     *     The folder
     */
    @JsonProperty("folder")
    @JacksonXmlProperty(localName = "folder", isAttribute = true)
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * agent configuration
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public AgentConfiguration getProcessClass() {
        return processClass;
    }

    /**
     * agent configuration
     * <p>
     * 
     * (Required)
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(AgentConfiguration processClass) {
        this.processClass = processClass;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(folder).append(processClass).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyAgent) == false) {
            return false;
        }
        ModifyAgent rhs = ((ModifyAgent) other);
        return new EqualsBuilder().append(folder, rhs.folder).append(processClass, rhs.processClass).isEquals();
    }

}
