
package com.sos.joc.model.joe.job;

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
@JacksonXmlRootElement(localName = "start_when_directory_changed")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "directory",
    "regex"
})
public class StartWhenDirectoryChanged {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    private String directory;
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    private String regex;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StartWhenDirectoryChanged() {
    }

    /**
     * 
     * @param regex
     * @param directory
     */
    public StartWhenDirectoryChanged(String directory, String regex) {
        this.directory = directory;
        this.regex = regex;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The directory
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public String getDirectory() {
        return directory;
    }

    /**
     * 
     * (Required)
     * 
     * @param directory
     *     The directory
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 
     * @return
     *     The regex
     */
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public String getRegex() {
        return regex;
    }

    /**
     * 
     * @param regex
     *     The regex
     */
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(regex).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartWhenDirectoryChanged) == false) {
            return false;
        }
        StartWhenDirectoryChanged rhs = ((StartWhenDirectoryChanged) other);
        return new EqualsBuilder().append(directory, rhs.directory).append(regex, rhs.regex).isEquals();
    }

}
