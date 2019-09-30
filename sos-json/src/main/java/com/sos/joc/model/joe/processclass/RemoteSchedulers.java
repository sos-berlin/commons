
package com.sos.joc.model.joe.processclass;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "remote_schedulers")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "select",
    "remoteSchedulerList"
})
public class RemoteSchedulers {

    /**
     * first, next
     * 
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    private String select;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("remoteSchedulerList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    private List<RemoteScheduler> remoteSchedulerList = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RemoteSchedulers() {
    }

    /**
     * 
     * @param select
     * @param remoteSchedulerList
     */
    public RemoteSchedulers(String select, List<RemoteScheduler> remoteSchedulerList) {
        this.select = select;
        this.remoteSchedulerList = remoteSchedulerList;
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
     * (Required)
     * 
     * @return
     *     The remoteSchedulerList
     */
    @JsonProperty("remoteSchedulerList")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    public List<RemoteScheduler> getRemoteSchedulerList() {
        return remoteSchedulerList;
    }

    /**
     * 
     * (Required)
     * 
     * @param remoteSchedulerList
     *     The remoteSchedulerList
     */
    @JsonProperty("remoteSchedulerList")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    public void setRemoteSchedulerList(List<RemoteScheduler> remoteSchedulerList) {
        this.remoteSchedulerList = remoteSchedulerList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(select).append(remoteSchedulerList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RemoteSchedulers) == false) {
            return false;
        }
        RemoteSchedulers rhs = ((RemoteSchedulers) other);
        return new EqualsBuilder().append(select, rhs.select).append(remoteSchedulerList, rhs.remoteSchedulerList).isEquals();
    }

}
