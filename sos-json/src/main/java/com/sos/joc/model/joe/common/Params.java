
package com.sos.joc.model.joe.common;

import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * parameters
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "params")
@JsonPropertyOrder({
    "includes",
    "copyParams",
    "paramList"
})
public class Params {

    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    private List<ParamInclude> includes = null;
    @JsonProperty("copyParams")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    private Set<CopyParams> copyParams = null;
    @JsonProperty("paramList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    private List<Param> paramList = null;

    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public List<ParamInclude> getIncludes() {
        return includes;
    }

    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "include", isAttribute = false)
    public void setIncludes(List<ParamInclude> includes) {
        this.includes = includes;
    }

    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public Set<CopyParams> getCopyParams() {
        return copyParams;
    }

    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public void setCopyParams(Set<CopyParams> copyParams) {
        this.copyParams = copyParams;
    }

    @JsonProperty("paramList")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public List<Param> getParamList() {
        return paramList;
    }

    @JsonProperty("paramList")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public void setParamList(List<Param> paramList) {
        this.paramList = paramList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("includes", includes).append("copyParams", copyParams).append("paramList", paramList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(paramList).append(includes).append(copyParams).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Params) == false) {
            return false;
        }
        Params rhs = ((Params) other);
        return new EqualsBuilder().append(paramList, rhs.paramList).append(includes, rhs.includes).append(copyParams, rhs.copyParams).isEquals();
    }

}
