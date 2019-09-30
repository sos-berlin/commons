
package com.sos.joc.model.joe.common;

import java.util.List;
import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
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
    /**
     * 
     */
    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    private CopyParams copyParams;
    @JsonProperty("includes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    private List<ParamInclude> includes = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Params() {
    }

    /**
     * 
     * @param copyParams
     * @param paramList
     * @param includes
     */
    public Params(List<Param> paramList, CopyParams copyParams, List<ParamInclude> includes) {
        this.paramList = paramList;
        this.copyParams = copyParams;
        this.includes = includes;
    }

    /**
     * 
     * @return
     *     The paramList
     */
    @JsonProperty("paramList")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public List<Param> getParamList() {
        return paramList;
    }

    /**
     * 
     * @param paramList
     *     The paramList
     */
    @JsonProperty("paramList")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public void setParamList(List<Param> paramList) {
        this.paramList = paramList;
    }

    /**
     * 
     * @return
     *     The copyParams
     */
    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public CopyParams getCopyParams() {
        return copyParams;
    }

    /**
     * 
     * @param copyParams
     *     The copyParams
     */
    @JsonProperty("copyParams")
    @JacksonXmlProperty(localName = "copy_params", isAttribute = false)
    public void setCopyParams(CopyParams copyParams) {
        this.copyParams = copyParams;
    }

    /**
     * 
     * @return
     *     The includes
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    public List<ParamInclude> getIncludes() {
        return includes;
    }

    /**
     * 
     * @param includes
     *     The includes
     */
    @JsonProperty("includes")
    @JacksonXmlProperty(localName = "includes", isAttribute = false)
    public void setIncludes(List<ParamInclude> includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(paramList).append(copyParams).append(includes).toHashCode();
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
        return new EqualsBuilder().append(paramList, rhs.paramList).append(copyParams, rhs.copyParams).append(includes, rhs.includes).isEquals();
    }

}
