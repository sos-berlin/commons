
package com.sos.joc.model.joe.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.Params;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order TODO
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "order")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "priority",
    "title",
    "state",
    "endState",
    "webService",
    "params",
    "runTime"
})
public class Order {

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    private String priority;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    private String endState;
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    private String webService;
    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private Params params;
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private String runTime = "";

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public void setPriority(String priority) {
        this.priority = priority;
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
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The endState
     */
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public String getEndState() {
        return endState;
    }

    /**
     * 
     * @param endState
     *     The endState
     */
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * 
     * @return
     *     The webService
     */
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public String getWebService() {
        return webService;
    }

    /**
     * 
     * @param webService
     *     The webService
     */
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public void setWebService(String webService) {
        this.webService = webService;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public Params getParams() {
        return params;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * 
     * @return
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public String getRunTime() {
        return runTime;
    }

    /**
     * 
     * @param runTime
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(priority).append(title).append(state).append(endState).append(webService).append(params).append(runTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order) == false) {
            return false;
        }
        Order rhs = ((Order) other);
        return new EqualsBuilder().append(priority, rhs.priority).append(title, rhs.title).append(state, rhs.state).append(endState, rhs.endState).append(webService, rhs.webService).append(params, rhs.params).append(runTime, rhs.runTime).isEquals();
    }

}
