
package com.sos.joc.model.joe.processclass;

import javax.annotation.Generated;
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
 * processClass without name, replace, spooler_id attributes
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "process_class")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "maxProcesses",
    "timeout",
    "remoteScheduler",
    "remoteSchedulers"
})
public class ProcessClass implements IJSObject
{

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    private Integer maxProcesses;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    private Integer timeout;
    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    private String remoteScheduler;
    /**
     * 
     */
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    private RemoteSchedulers remoteSchedulers;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProcessClass() {
    }

    /**
     * 
     * @param maxProcesses
     * @param remoteSchedulers
     * @param remoteScheduler
     * @param timeout
     */
    public ProcessClass(Integer maxProcesses, Integer timeout, String remoteScheduler, RemoteSchedulers remoteSchedulers) {
        this.maxProcesses = maxProcesses;
        this.timeout = timeout;
        this.remoteScheduler = remoteScheduler;
        this.remoteSchedulers = remoteSchedulers;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The maxProcesses
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    public Integer getMaxProcesses() {
        return maxProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param maxProcesses
     *     The maxProcesses
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    public void setMaxProcesses(Integer maxProcesses) {
        this.maxProcesses = maxProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The timeout
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param timeout
     *     The timeout
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * 
     * @return
     *     The remoteScheduler
     */
    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    public String getRemoteScheduler() {
        return remoteScheduler;
    }

    /**
     * 
     * @param remoteScheduler
     *     The remoteScheduler
     */
    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    public void setRemoteScheduler(String remoteScheduler) {
        this.remoteScheduler = remoteScheduler;
    }

    /**
     * 
     * @return
     *     The remoteSchedulers
     */
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    public RemoteSchedulers getRemoteSchedulers() {
        return remoteSchedulers;
    }

    /**
     * 
     * @param remoteSchedulers
     *     The remoteSchedulers
     */
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    public void setRemoteSchedulers(RemoteSchedulers remoteSchedulers) {
        this.remoteSchedulers = remoteSchedulers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(maxProcesses).append(timeout).append(remoteScheduler).append(remoteSchedulers).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessClass) == false) {
            return false;
        }
        ProcessClass rhs = ((ProcessClass) other);
        return new EqualsBuilder().append(maxProcesses, rhs.maxProcesses).append(timeout, rhs.timeout).append(remoteScheduler, rhs.remoteScheduler).append(remoteSchedulers, rhs.remoteSchedulers).isEquals();
    }

}
