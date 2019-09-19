
package com.sos.joc.model.joe.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * add order
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "add_order")
@JsonPropertyOrder({
    "jobChain",
    "id",
    "at",
    "replace",
    "suspended",
    "runTime"
})
public class AddOrder
    extends Order
{

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
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    private String at;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("replace")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    private String replace;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("suspended")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    private String suspended;
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private String runTime;

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

    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public String getAt() {
        return at;
    }

    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("replace")
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    public String getReplace() {
        return replace;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("replace")
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    public void setReplace(String replace) {
        this.replace = replace;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("suspended")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    public String getSuspended() {
        return suspended;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("suspended")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }

    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public String getRunTime() {
        return runTime;
    }

    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("jobChain", jobChain).append("id", id).append("at", at).append("replace", replace).append("suspended", suspended).append("runTime", runTime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(at).append(jobChain).append(replace).append(id).append(runTime).append(suspended).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AddOrder) == false) {
            return false;
        }
        AddOrder rhs = ((AddOrder) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(at, rhs.at).append(jobChain, rhs.jobChain).append(replace, rhs.replace).append(id, rhs.id).append(runTime, rhs.runTime).append(suspended, rhs.suspended).isEquals();
    }

}
