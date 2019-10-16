
package com.sos.joc.model.joe.common;

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


/**
 * parameters
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "params")
@JsonPropertyOrder({
    "paramList",
    "copyParams",
    "includes"
})
public class Params {

    @JsonProperty("paramList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    private List<Param> paramList = null;
    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    private CopyParams copyParams;
    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    private List<ParamInclude> includes = null;

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

    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public CopyParams getCopyParams() {
        return copyParams;
    }

    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public void setCopyParams(CopyParams copyParams) {
        this.copyParams = copyParams;
    }

    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    public List<ParamInclude> getIncludes() {
        return includes;
    }

    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    public void setIncludes(List<ParamInclude> includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("paramList", paramList).append("copyParams", copyParams).append("includes", includes).toString();
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
