
package com.sos.joc.model.joe.jobchain;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain without name, replace attributes
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_chain")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "ordersRecoverable",
    "title",
    "maxOrders",
    "distributed",
    "processClass",
    "fileWatchingProcessClass",
    "visible",
    "fileOrderSources",
    "jobChainNodes",
    "fileOrderSinks",
    "nestedJobChainNodes",
    "jobChainEndNodes"
})
public class JobChain implements IJSObject
{

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("ordersRecoverable")
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
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    private String distributed;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private String processClass;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("fileWatchingProcessClass")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    private String fileWatchingProcessClass;
    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    private String visible;
    @JsonProperty("fileOrderSources")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "file_order_source", isAttribute = false)
    private List<FileOrderSource> fileOrderSources = null;
    @JsonProperty("jobChainNodes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chain_node", isAttribute = false)
    private List<JobChainNode> jobChainNodes = null;
    @JsonProperty("fileOrderSinks")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "file_order_sink", isAttribute = false)
    private List<FileOrderSink> fileOrderSinks = null;
    @JsonProperty("nestedJobChainNodes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chain_node.job_chain", isAttribute = false)
    private List<NestedJobChainNode> nestedJobChainNodes = null;
    @JsonProperty("jobChainEndNodes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job_chain_node.end", isAttribute = false)
    private List<JobChainEndNode> jobChainEndNodes = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JobChain() {
    }

    /**
     * 
     * @param ordersRecoverable
     * @param fileOrderSources
     * @param visible
     * @param jobChainNodes
     * @param distributed
     * @param nestedJobChainNodes
     * @param processClass
     * @param title
     * @param maxOrders
     * @param fileOrderSinks
     * @param jobChainEndNodes
     * @param fileWatchingProcessClass
     */
    public JobChain(String ordersRecoverable, String title, Integer maxOrders, String distributed, String processClass, String fileWatchingProcessClass, String visible, List<FileOrderSource> fileOrderSources, List<JobChainNode> jobChainNodes, List<FileOrderSink> fileOrderSinks, List<NestedJobChainNode> nestedJobChainNodes, List<JobChainEndNode> jobChainEndNodes) {
        this.ordersRecoverable = ordersRecoverable;
        this.title = title;
        this.maxOrders = maxOrders;
        this.distributed = distributed;
        this.processClass = processClass;
        this.fileWatchingProcessClass = fileWatchingProcessClass;
        this.visible = visible;
        this.fileOrderSources = fileOrderSources;
        this.jobChainNodes = jobChainNodes;
        this.fileOrderSinks = fileOrderSinks;
        this.nestedJobChainNodes = nestedJobChainNodes;
        this.jobChainEndNodes = jobChainEndNodes;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The ordersRecoverable
     */
    @JsonProperty("ordersRecoverable")
    @JacksonXmlProperty(localName = "orders_recoverable", isAttribute = true)
    public String getOrdersRecoverable() {
        return ordersRecoverable;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param ordersRecoverable
     *     The ordersRecoverable
     */
    @JsonProperty("ordersRecoverable")
    @JacksonXmlProperty(localName = "orders_recoverable", isAttribute = true)
    public void setOrdersRecoverable(String ordersRecoverable) {
        this.ordersRecoverable = ordersRecoverable;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
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
     * @return
     *     The maxOrders
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
     * @param maxOrders
     *     The maxOrders
     */
    @JsonProperty("maxOrders")
    @JacksonXmlProperty(localName = "max_orders", isAttribute = true)
    public void setMaxOrders(Integer maxOrders) {
        this.maxOrders = maxOrders;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The distributed
     */
    @JsonProperty("distributed")
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    public String getDistributed() {
        return distributed;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param distributed
     *     The distributed
     */
    @JsonProperty("distributed")
    @JacksonXmlProperty(localName = "distributed", isAttribute = true)
    public void setDistributed(String distributed) {
        this.distributed = distributed;
    }

    /**
     * path of a process class object
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path of a process class object
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * path of a process class object
     * 
     * @return
     *     The fileWatchingProcessClass
     */
    @JsonProperty("fileWatchingProcessClass")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    public String getFileWatchingProcessClass() {
        return fileWatchingProcessClass;
    }

    /**
     * path of a process class object
     * 
     * @param fileWatchingProcessClass
     *     The fileWatchingProcessClass
     */
    @JsonProperty("fileWatchingProcessClass")
    @JacksonXmlProperty(localName = "file_watching_process_class", isAttribute = true)
    public void setFileWatchingProcessClass(String fileWatchingProcessClass) {
        this.fileWatchingProcessClass = fileWatchingProcessClass;
    }

    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     * @return
     *     The visible
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    public String getVisible() {
        return visible;
    }

    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     * @param visible
     *     The visible
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    @JacksonXmlProperty(localName = "file_order_source", isAttribute = false)
    public List<FileOrderSource> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    @JacksonXmlProperty(localName = "file_order_source", isAttribute = false)
    public void setFileOrderSources(List<FileOrderSource> fileOrderSources) {
        this.fileOrderSources = fileOrderSources;
    }

    /**
     * 
     * @return
     *     The jobChainNodes
     */
    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "job_chain_node", isAttribute = false)
    public List<JobChainNode> getJobChainNodes() {
        return jobChainNodes;
    }

    /**
     * 
     * @param jobChainNodes
     *     The jobChainNodes
     */
    @JsonProperty("jobChainNodes")
    @JacksonXmlProperty(localName = "job_chain_node", isAttribute = false)
    public void setJobChainNodes(List<JobChainNode> jobChainNodes) {
        this.jobChainNodes = jobChainNodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSinks
     */
    @JsonProperty("fileOrderSinks")
    @JacksonXmlProperty(localName = "file_order_sink", isAttribute = false)
    public List<FileOrderSink> getFileOrderSinks() {
        return fileOrderSinks;
    }

    /**
     * 
     * @param fileOrderSinks
     *     The fileOrderSinks
     */
    @JsonProperty("fileOrderSinks")
    @JacksonXmlProperty(localName = "file_order_sink", isAttribute = false)
    public void setFileOrderSinks(List<FileOrderSink> fileOrderSinks) {
        this.fileOrderSinks = fileOrderSinks;
    }

    /**
     * 
     * @return
     *     The nestedJobChainNodes
     */
    @JsonProperty("nestedJobChainNodes")
    @JacksonXmlProperty(localName = "job_chain_node.job_chain", isAttribute = false)
    public List<NestedJobChainNode> getNestedJobChainNodes() {
        return nestedJobChainNodes;
    }

    /**
     * 
     * @param nestedJobChainNodes
     *     The nestedJobChainNodes
     */
    @JsonProperty("nestedJobChainNodes")
    @JacksonXmlProperty(localName = "job_chain_node.job_chain", isAttribute = false)
    public void setNestedJobChainNodes(List<NestedJobChainNode> nestedJobChainNodes) {
        this.nestedJobChainNodes = nestedJobChainNodes;
    }

    /**
     * 
     * @return
     *     The jobChainEndNodes
     */
    @JsonProperty("jobChainEndNodes")
    @JacksonXmlProperty(localName = "job_chain_node.end", isAttribute = false)
    public List<JobChainEndNode> getJobChainEndNodes() {
        return jobChainEndNodes;
    }

    /**
     * 
     * @param jobChainEndNodes
     *     The jobChainEndNodes
     */
    @JsonProperty("jobChainEndNodes")
    @JacksonXmlProperty(localName = "job_chain_node.end", isAttribute = false)
    public void setJobChainEndNodes(List<JobChainEndNode> jobChainEndNodes) {
        this.jobChainEndNodes = jobChainEndNodes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ordersRecoverable).append(title).append(maxOrders).append(distributed).append(processClass).append(fileWatchingProcessClass).append(visible).append(fileOrderSources).append(jobChainNodes).append(fileOrderSinks).append(nestedJobChainNodes).append(jobChainEndNodes).toHashCode();
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
        return new EqualsBuilder().append(ordersRecoverable, rhs.ordersRecoverable).append(title, rhs.title).append(maxOrders, rhs.maxOrders).append(distributed, rhs.distributed).append(processClass, rhs.processClass).append(fileWatchingProcessClass, rhs.fileWatchingProcessClass).append(visible, rhs.visible).append(fileOrderSources, rhs.fileOrderSources).append(jobChainNodes, rhs.jobChainNodes).append(fileOrderSinks, rhs.fileOrderSinks).append(nestedJobChainNodes, rhs.nestedJobChainNodes).append(jobChainEndNodes, rhs.jobChainEndNodes).isEquals();
    }

}
