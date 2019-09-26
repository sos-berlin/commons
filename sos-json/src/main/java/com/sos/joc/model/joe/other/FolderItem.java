
package com.sos.joc.model.joe.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * folder item
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "folder_item")
@JsonPropertyOrder({
    "name",
    "deployed"
})
public class FolderItem {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    private Boolean deployed = false;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FolderItem() {
    }

    /**
     * 
     * @param name
     * @param deployed
     */
    public FolderItem(String name, Boolean deployed) {
        super();
        this.name = name;
        this.deployed = deployed;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("deployed", deployed).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FolderItem) == false) {
            return false;
        }
        FolderItem rhs = ((FolderItem) other);
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

}
