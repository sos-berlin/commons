
package com.sos.joc.model.joe.job;

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
     * 
     * (Required)
     * 
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
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public String getRegex() {
        return regex;
    }

    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("directory", directory).append("regex", regex).toString();
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
