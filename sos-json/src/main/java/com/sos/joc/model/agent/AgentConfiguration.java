
package com.sos.joc.model.agent;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent configuration
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
    "select",
    "remoteScheduler",
    "remoteSchedulers"
})
public class AgentConfiguration {

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
     * first, next
     * 
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    private String select;
    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    private String remoteScheduler;
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers")
    @JacksonXmlElementWrapper(useWrapping = true, localName = "remote_schedulers")
    private List<RemoteScheduler> remoteSchedulers = null;

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
     * first, next
     * 
     * @return
     *     The select
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    public String getSelect() {
        return select;
    }

    /**
     * first, next
     * 
     * @param select
     *     The select
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    public void setSelect(String select) {
        this.select = select;
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
    @JacksonXmlProperty(localName = "remote_schedulers")
    public List<RemoteScheduler> getRemoteSchedulers() {
        return remoteSchedulers;
    }

    /**
     * 
     * @param remoteSchedulers
     *     The remoteSchedulers
     */
    @JsonProperty("remoteSchedulers")
    @JacksonXmlProperty(localName = "remote_schedulers")
    public void setRemoteSchedulers(List<RemoteScheduler> remoteSchedulers) {
        this.remoteSchedulers = remoteSchedulers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(maxProcesses).append(select).append(remoteScheduler).append(remoteSchedulers).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentConfiguration) == false) {
            return false;
        }
        AgentConfiguration rhs = ((AgentConfiguration) other);
        return new EqualsBuilder().append(name, rhs.name).append(maxProcesses, rhs.maxProcesses).append(select, rhs.select).append(remoteScheduler, rhs.remoteScheduler).append(remoteSchedulers, rhs.remoteSchedulers).isEquals();
    }

}
