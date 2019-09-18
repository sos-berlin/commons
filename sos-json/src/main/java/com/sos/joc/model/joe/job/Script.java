
package com.sos.joc.model.joe.job;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job script TODO it's incomplete
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "script")
@JsonPropertyOrder({
    "language",
    "javaClass",
    "javaClassPath",
    "includes",
    "content"
})
public class Script {

    @JsonProperty("language")
    @JacksonXmlProperty(localName = "language", isAttribute = true)
    private String language;
    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    private String javaClass;
    @JsonProperty("javaClassPath")
    @JacksonXmlProperty(localName = "java_class_path", isAttribute = true)
    private String javaClassPath;
    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    private List<com.sos.joc.model.joe.common.Include> includes = null;
    /**
     * cdata embedded script, e.g. javascript
     * 
     */
    @JsonProperty("content")
    @JsonPropertyDescription("cdata embedded script, e.g. javascript")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    private String content;

    @JsonProperty("language")
    @JacksonXmlProperty(localName = "language", isAttribute = true)
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    @JacksonXmlProperty(localName = "language", isAttribute = true)
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    public String getJavaClass() {
        return javaClass;
    }

    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    @JsonProperty("javaClassPath")
    @JacksonXmlProperty(localName = "java_class_path", isAttribute = true)
    public String getJavaClassPath() {
        return javaClassPath;
    }

    @JsonProperty("javaClassPath")
    @JacksonXmlProperty(localName = "java_class_path", isAttribute = true)
    public void setJavaClassPath(String javaClassPath) {
        this.javaClassPath = javaClassPath;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public List<com.sos.joc.model.joe.common.Include> getIncludes() {
        return includes;
    }

    /**
     * include collection
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setIncludes(List<com.sos.joc.model.joe.common.Include> includes) {
        this.includes = includes;
    }

    /**
     * cdata embedded script, e.g. javascript
     * 
     */
    @JsonProperty("content")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    public String getContent() {
        return content;
    }

    /**
     * cdata embedded script, e.g. javascript
     * 
     */
    @JsonProperty("content")
    @JacksonXmlText
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "content", isAttribute = false)
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("language", language).append("javaClass", javaClass).append("javaClassPath", javaClassPath).append("includes", includes).append("content", content).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(javaClassPath).append(language).append(includes).append(javaClass).append(content).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Script) == false) {
            return false;
        }
        Script rhs = ((Script) other);
        return new EqualsBuilder().append(javaClassPath, rhs.javaClassPath).append(language, rhs.language).append(includes, rhs.includes).append(javaClass, rhs.javaClass).append(content, rhs.content).isEquals();
    }

}
