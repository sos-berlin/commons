
package com.sos.joc.model.joe.wizard;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "job")
@JsonPropertyOrder({
    "deliveryDate",
    "jitlPath",
    "jitlName",
    "title",
    "javaClass",
    "params"
})
public class Job {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jitlPath")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "jitl_path", isAttribute = true)
    private String jitlPath;
    @JsonProperty("jitlName")
    @JacksonXmlProperty(localName = "jitl_name", isAttribute = true)
    private String jitlName;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    private String javaClass;
    @JsonProperty("params")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private List<Param> params = null;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jitlPath")
    @JacksonXmlProperty(localName = "jitl_path", isAttribute = true)
    public String getJitlPath() {
        return jitlPath;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jitlPath")
    @JacksonXmlProperty(localName = "jitl_path", isAttribute = true)
    public void setJitlPath(String jitlPath) {
        this.jitlPath = jitlPath;
    }

    @JsonProperty("jitlName")
    @JacksonXmlProperty(localName = "jitl_name", isAttribute = true)
    public String getJitlName() {
        return jitlName;
    }

    @JsonProperty("jitlName")
    @JacksonXmlProperty(localName = "jitl_name", isAttribute = true)
    public void setJitlName(String jitlName) {
        this.jitlName = jitlName;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    public String getJavaClass() {
        return javaClass;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("javaClass")
    @JacksonXmlProperty(localName = "java_class", isAttribute = true)
    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public List<Param> getParams() {
        return params;
    }

    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(List<Param> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("jitlPath", jitlPath).append("jitlName", jitlName).append("title", title).append("javaClass", javaClass).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jitlName).append(javaClass).append(jitlPath).append(deliveryDate).append(title).append(params).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job) == false) {
            return false;
        }
        Job rhs = ((Job) other);
        return new EqualsBuilder().append(jitlName, rhs.jitlName).append(javaClass, rhs.javaClass).append(jitlPath, rhs.jitlPath).append(deliveryDate, rhs.deliveryDate).append(title, rhs.title).append(params, rhs.params).isEquals();
    }

}
