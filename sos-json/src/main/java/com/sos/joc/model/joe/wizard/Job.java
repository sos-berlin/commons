
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
    "docPath",
    "docName",
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
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    private String docPath;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("docName")
    @JacksonXmlProperty(localName = "doc_name", isAttribute = true)
    private String docName;
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
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    public String getDocPath() {
        return docPath;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("docName")
    @JacksonXmlProperty(localName = "doc_name", isAttribute = true)
    public String getDocName() {
        return docName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("docName")
    @JacksonXmlProperty(localName = "doc_name", isAttribute = true)
    public void setDocName(String docName) {
        this.docName = docName;
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
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("docPath", docPath).append("docName", docName).append("title", title).append("javaClass", javaClass).append("params", params).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(docName).append(javaClass).append(deliveryDate).append(title).append(params).append(docPath).toHashCode();
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
        return new EqualsBuilder().append(docName, rhs.docName).append(javaClass, rhs.javaClass).append(deliveryDate, rhs.deliveryDate).append(title, rhs.title).append(params, rhs.params).append(docPath, rhs.docPath).isEquals();
    }

}
