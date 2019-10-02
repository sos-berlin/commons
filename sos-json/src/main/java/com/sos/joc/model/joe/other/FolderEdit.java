
package com.sos.joc.model.joe.other;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.job.Job;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit folders
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "folder_edit")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "configuration"
})
public class FolderEdit
    extends JSObjectEdit
{

    /**
     * job without name, vtemporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet, replace attributes
     * <p>
     * 
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    private Job configuration;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FolderEdit() {
    }

    /**
     * 
     * @param configuration
     */
    public FolderEdit(Job configuration) {
        this.configuration = configuration;
    }

    /**
     * job without name, vtemporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet, replace attributes
     * <p>
     * 
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public Job getConfiguration() {
        return configuration;
    }

    /**
     * job without name, vtemporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet, replace attributes
     * <p>
     * 
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public void setConfiguration(Job configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
        if ((other instanceof FolderEdit) == false) {
            return false;
        }
        FolderEdit rhs = ((FolderEdit) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(configuration, rhs.configuration).isEquals();
    }

}
