
package com.sos.joc.model.joe.common;

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
 * include for parameters
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "include")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "file",
    "liveFile",
    "node"
})
public class ParamInclude {

    @JsonProperty("file")
    @JacksonXmlProperty(localName = "file", isAttribute = true)
    private String file;
    @JsonProperty("liveFile")
    @JacksonXmlProperty(localName = "live_file", isAttribute = true)
    private String liveFile;
    @JsonProperty("node")
    @JacksonXmlProperty(localName = "node", isAttribute = true)
    private String node;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ParamInclude() {
    }

    /**
     * 
     * @param liveFile
     * @param node
     * @param file
     */
    public ParamInclude(String file, String liveFile, String node) {
        this.file = file;
        this.liveFile = liveFile;
        this.node = node;
    }

    /**
     * 
     * @return
     *     The file
     */
    @JsonProperty("file")
    @JacksonXmlProperty(localName = "file", isAttribute = true)
    public String getFile() {
        return file;
    }

    /**
     * 
     * @param file
     *     The file
     */
    @JsonProperty("file")
    @JacksonXmlProperty(localName = "file", isAttribute = true)
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * 
     * @return
     *     The liveFile
     */
    @JsonProperty("liveFile")
    @JacksonXmlProperty(localName = "live_file", isAttribute = true)
    public String getLiveFile() {
        return liveFile;
    }

    /**
     * 
     * @param liveFile
     *     The liveFile
     */
    @JsonProperty("liveFile")
    @JacksonXmlProperty(localName = "live_file", isAttribute = true)
    public void setLiveFile(String liveFile) {
        this.liveFile = liveFile;
    }

    /**
     * 
     * @return
     *     The node
     */
    @JsonProperty("node")
    @JacksonXmlProperty(localName = "node", isAttribute = true)
    public String getNode() {
        return node;
    }

    /**
     * 
     * @param node
     *     The node
     */
    @JsonProperty("node")
    @JacksonXmlProperty(localName = "node", isAttribute = true)
    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(file).append(liveFile).append(node).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ParamInclude) == false) {
            return false;
        }
        ParamInclude rhs = ((ParamInclude) other);
        return new EqualsBuilder().append(file, rhs.file).append(liveFile, rhs.liveFile).append(node, rhs.node).isEquals();
    }

}
