
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
    "params",
    "copyParams",
    "includes"
})
public class Params {

    @JsonProperty("params")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    private List<Param> params = null;
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
     * 
     * @return
     *     The params
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public List<Param> getParams() {
        return params;
    }

    /**
     * 
     * @param params
     *     The params
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    public void setParams(List<Param> params) {
        this.params = params;
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
        return new HashCodeBuilder().append(params).append(copyParams).append(includes).toHashCode();
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
        return new EqualsBuilder().append(params, rhs.params).append(copyParams, rhs.copyParams).append(includes, rhs.includes).isEquals();
    }

}
