
package com.sos.joc.model.joe.nodeparams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * node params config file
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "settings")
@JsonPropertyOrder({
    "jobChain"
})
public class Config implements IJSObject
{

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = false)
    private ConfigJobChain jobChain;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = false)
    public ConfigJobChain getJobChain() {
        return jobChain;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = false)
    public void setJobChain(ConfigJobChain jobChain) {
        this.jobChain = jobChain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobChain", jobChain).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChain).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Config) == false) {
            return false;
        }
        Config rhs = ((Config) other);
        return new EqualsBuilder().append(jobChain, rhs.jobChain).isEquals();
    }

}
