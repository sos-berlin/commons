
package com.sos.joc.model.joe.processClass;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.processClass.RemoteSchedulers;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * processClass configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "process_class")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "maxProcesses",
    "timeout",
    "remoteScheduler",
    "remoteSchedulers"
})
public class ProcessClassConfiguration {

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
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
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
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
        return new HashCodeBuilder().append(name).append(maxProcesses).append(timeout).append(remoteScheduler).append(remoteSchedulers).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessClassConfiguration) == false) {
            return false;
        }
        ProcessClassConfiguration rhs = ((ProcessClassConfiguration) other);
        return new EqualsBuilder().append(name, rhs.name).append(maxProcesses, rhs.maxProcesses).append(timeout, rhs.timeout).append(remoteScheduler, rhs.remoteScheduler).append(remoteSchedulers, rhs.remoteSchedulers).isEquals();
    }

}
