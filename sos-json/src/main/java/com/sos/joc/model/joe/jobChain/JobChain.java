
package com.sos.joc.model.joe.jobChain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_chain")
@JsonPropertyOrder({
    "ordersRecoverable",
    "title",
    "maxOrders",
    "distributed",
    "processClass",
    "fileWatchingProcessClass"
})
public class JobChain implements IJSObject
{

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("ordersRecoverable")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "orders_recoverable", isAttribute = true)
    private String ordersRecoverable;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxOrders")
    @JacksonXmlProperty(localName = "max_orders", isAttribute = true)
    private Integer maxOrders;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("distributed")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    private String distributed;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JsonPropertyDescription("path of a process class object")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private String processClass;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("fileWatchingProcessClass")
    @JsonPropertyDescription("path of a process class object")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    private String fileWatchingProcessClass;

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("ordersRecoverable")
    @JacksonXmlProperty(localName = "orders_recoverable", isAttribute = true)
    public String getOrdersRecoverable() {
        return ordersRecoverable;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("ordersRecoverable")
    @JacksonXmlProperty(localName = "orders_recoverable", isAttribute = true)
    public void setOrdersRecoverable(String ordersRecoverable) {
        this.ordersRecoverable = ordersRecoverable;
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
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxOrders")
    @JacksonXmlProperty(localName = "max_orders", isAttribute = true)
    public Integer getMaxOrders() {
        return maxOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxOrders")
    @JacksonXmlProperty(localName = "max_orders", isAttribute = true)
    public void setMaxOrders(Integer maxOrders) {
        this.maxOrders = maxOrders;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("distributed")
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    public String getDistributed() {
        return distributed;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("distributed")
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    public void setDistributed(String distributed) {
        this.distributed = distributed;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("fileWatchingProcessClass")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    public String getFileWatchingProcessClass() {
        return fileWatchingProcessClass;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("fileWatchingProcessClass")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    public void setFileWatchingProcessClass(String fileWatchingProcessClass) {
        this.fileWatchingProcessClass = fileWatchingProcessClass;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ordersRecoverable", ordersRecoverable).append("title", title).append("maxOrders", maxOrders).append("distributed", distributed).append("processClass", processClass).append("fileWatchingProcessClass", fileWatchingProcessClass).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ordersRecoverable).append(distributed).append(processClass).append(title).append(maxOrders).append(fileWatchingProcessClass).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChain) == false) {
            return false;
        }
        JobChain rhs = ((JobChain) other);
        return new EqualsBuilder().append(ordersRecoverable, rhs.ordersRecoverable).append(distributed, rhs.distributed).append(processClass, rhs.processClass).append(title, rhs.title).append(maxOrders, rhs.maxOrders).append(fileWatchingProcessClass, rhs.fileWatchingProcessClass).isEquals();
    }

}
