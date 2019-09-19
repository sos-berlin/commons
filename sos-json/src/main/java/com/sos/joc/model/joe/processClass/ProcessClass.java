
package com.sos.joc.model.joe.processClass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.processClass.RemoteSchedulers;
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
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    private RemoteSchedulers remoteSchedulers;

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
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
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    public String getRemoteScheduler() {
        return remoteScheduler;
    }

    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    public void setRemoteScheduler(String remoteScheduler) {
        this.remoteScheduler = remoteScheduler;
    }

    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    public RemoteSchedulers getRemoteSchedulers() {
        return remoteSchedulers;
    }

    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers", isAttribute = false)
    public void setRemoteSchedulers(RemoteSchedulers remoteSchedulers) {
        this.remoteSchedulers = remoteSchedulers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("maxProcesses", maxProcesses).append("timeout", timeout).append("remoteScheduler", remoteScheduler).append("remoteSchedulers", remoteSchedulers).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(maxProcesses).append(remoteSchedulers).append(remoteScheduler).append(timeout).toHashCode();
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
        return new EqualsBuilder().append(maxProcesses, rhs.maxProcesses).append(remoteSchedulers, rhs.remoteSchedulers).append(remoteScheduler, rhs.remoteScheduler).append(timeout, rhs.timeout).isEquals();
    }

}
