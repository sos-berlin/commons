
package com.sos.joc.model.joe.wizzard;

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
    "path",
    "name",
    "title",
    "javaClass",
    "description",
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
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
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
    /**
     * string in html format
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("string in html format")
    @JacksonXmlProperty(localName = "description", isAttribute = true)
    private String description;
    @JsonProperty("params")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private List<Params> params = null;

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
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
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

    /**
     * string in html format
     * 
     */
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description", isAttribute = true)
    public String getDescription() {
        return description;
    }

    /**
     * string in html format
     * 
     */
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description", isAttribute = true)
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public List<Params> getParams() {
        return params;
    }

    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(List<Params> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("path", path).append("name", name).append("title", title).append("javaClass", javaClass).append("description", description).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(javaClass).append(name).append(description).append(deliveryDate).append(title).append(params).toHashCode();
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
        return new EqualsBuilder().append(path, rhs.path).append(javaClass, rhs.javaClass).append(name, rhs.name).append(description, rhs.description).append(deliveryDate, rhs.deliveryDate).append(title, rhs.title).append(params, rhs.params).isEquals();
    }

}
