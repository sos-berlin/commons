
package com.sos.joc.model.joe.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.Params;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * add order in return code
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "return_code_add_order")
@JsonPropertyOrder({
    "xmlns",
    "jobChain",
    "id",
    "params"
})
public class ReturnCodeAddOrder {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    private String xmlns = "https://jobscheduler-plugins.sos-berlin.com/NodeOrderPlugin";
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    private String jobChain;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private String id;
    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private Params params;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    public String getXmlns() {
        return xmlns;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("xmlns")
    @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    public String getJobChain() {
        return jobChain;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    public String getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    public void setId(String id) {
        this.id = id;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public Params getParams() {
        return params;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("xmlns", xmlns).append("jobChain", jobChain).append("id", id).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChain).append(xmlns).append(id).append(params).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ReturnCodeAddOrder) == false) {
            return false;
        }
        ReturnCodeAddOrder rhs = ((ReturnCodeAddOrder) other);
        return new EqualsBuilder().append(jobChain, rhs.jobChain).append(xmlns, rhs.xmlns).append(id, rhs.id).append(params, rhs.params).isEquals();
    }

}
