
package com.sos.joc.model.joe.nodeparams;

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
 * job chain node parameters
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "params")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "paramList"
})
public class Params {

    @JsonProperty("paramList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "param", isAttribute = false)
    private List<Param> paramList = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Params() {
    }

    /**
     * 
     * @param paramList
     */
    public Params(List<Param> paramList) {
        this.paramList = paramList;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(paramList).toHashCode();
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
        return new EqualsBuilder().append(paramList, rhs.paramList).isEquals();
    }

}
