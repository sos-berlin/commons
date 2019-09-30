
package com.sos.joc.model.joe.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobChain",
    "id",
    "at",
    "replace",
    "suspended"
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
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    private String replace;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("suspended")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    private String suspended;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AddOrder() {
    }

    /**
     * 
     * @param at
     * @param jobChain
     * @param replace
     * @param id
     * @param suspended
     */
    public AddOrder(String jobChain, String id, String at, String replace, String suspended) {
        this.jobChain = jobChain;
        this.id = id;
        this.at = at;
        this.replace = replace;
        this.suspended = suspended;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobChain
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
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "job_chain", isAttribute = true)
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The at
     */
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public String getAt() {
        return at;
    }

    /**
     * 
     * @param at
     *     The at
     */
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The replace
     */
    @JsonProperty("replace")
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    public String getReplace() {
        return replace;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param replace
     *     The replace
     */
    @JsonProperty("replace")
    @JacksonXmlProperty(localName = "replace", isAttribute = true)
    public void setReplace(String replace) {
        this.replace = replace;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The suspended
     */
    @JsonProperty("suspended")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    public String getSuspended() {
        return suspended;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param suspended
     *     The suspended
     */
    @JsonProperty("suspended")
    @JacksonXmlProperty(localName = "suspended", isAttribute = true)
    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(jobChain).append(id).append(at).append(replace).append(suspended).toHashCode();
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
        return new EqualsBuilder().appendSuper(super.equals(other)).append(jobChain, rhs.jobChain).append(id, rhs.id).append(at, rhs.at).append(replace, rhs.replace).append(suspended, rhs.suspended).isEquals();
    }

}
