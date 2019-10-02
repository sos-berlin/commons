
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
 * List of deployables
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "deployables")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deployables"
})
public class Deployables {

    @JsonProperty("deployables")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    private List<Deployable> deployables = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Deployables() {
    }

    /**
     * 
     * @param deployables
     */
    public Deployables(List<Deployable> deployables) {
        this.deployables = deployables;
    }

    /**
     * 
     * @return
     *     The deployables
     */
    @JsonProperty("deployables")
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    public List<Deployable> getDeployables() {
        return deployables;
    }

    /**
     * 
     * @param deployables
     *     The deployables
     */
    @JsonProperty("deployables")
    @JacksonXmlProperty(localName = "deployables", isAttribute = false)
    public void setDeployables(List<Deployable> deployables) {
        this.deployables = deployables;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deployables).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Deployables) == false) {
            return false;
        }
        Deployables rhs = ((Deployables) other);
        return new EqualsBuilder().append(deployables, rhs.deployables).isEquals();
    }

}
