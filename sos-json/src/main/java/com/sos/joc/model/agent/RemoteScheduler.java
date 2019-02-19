
package com.sos.joc.model.agent;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "remote_scheduler")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "remoteScheduler",
    "httpHeartbeatTimeout",
    "httpHeartbeatPeriod"
})
public class RemoteScheduler {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("remoteScheduler")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = true)
    private String remoteScheduler;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("httpHeartbeatTimeout")
    @JacksonXmlProperty(localName = "http_heartbeat_timeout", isAttribute = true)
    private Integer httpHeartbeatTimeout;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("httpHeartbeatPeriod")
    @JacksonXmlProperty(localName = "http_heartbeat_period", isAttribute = true)
    private Integer httpHeartbeatPeriod;

    /**
     * 
     * (Required)
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
     * (Required)
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The httpHeartbeatTimeout
     */
    @JsonProperty("httpHeartbeatTimeout")
    @JacksonXmlProperty(localName = "http_heartbeat_timeout", isAttribute = true)
    public Integer getHttpHeartbeatTimeout() {
        return httpHeartbeatTimeout;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param httpHeartbeatTimeout
     *     The httpHeartbeatTimeout
     */
    @JsonProperty("httpHeartbeatTimeout")
    @JacksonXmlProperty(localName = "http_heartbeat_timeout", isAttribute = true)
    public void setHttpHeartbeatTimeout(Integer httpHeartbeatTimeout) {
        this.httpHeartbeatTimeout = httpHeartbeatTimeout;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The httpHeartbeatPeriod
     */
    @JsonProperty("httpHeartbeatPeriod")
    @JacksonXmlProperty(localName = "http_heartbeat_period", isAttribute = true)
    public Integer getHttpHeartbeatPeriod() {
        return httpHeartbeatPeriod;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param httpHeartbeatPeriod
     *     The httpHeartbeatPeriod
     */
    @JsonProperty("httpHeartbeatPeriod")
    @JacksonXmlProperty(localName = "http_heartbeat_period", isAttribute = true)
    public void setHttpHeartbeatPeriod(Integer httpHeartbeatPeriod) {
        this.httpHeartbeatPeriod = httpHeartbeatPeriod;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(remoteScheduler).append(httpHeartbeatTimeout).append(httpHeartbeatPeriod).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RemoteScheduler) == false) {
            return false;
        }
        RemoteScheduler rhs = ((RemoteScheduler) other);
        return new EqualsBuilder().append(remoteScheduler, rhs.remoteScheduler).append(httpHeartbeatTimeout, rhs.httpHeartbeatTimeout).append(httpHeartbeatPeriod, rhs.httpHeartbeatPeriod).isEquals();
    }

}
