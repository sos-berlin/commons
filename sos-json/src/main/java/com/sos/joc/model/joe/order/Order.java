
package com.sos.joc.model.joe.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.schedule.RunTime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "order")
@JsonPropertyOrder({
    "priority",
    "title",
    "state",
    "endState",
    "webService",
    "params",
    "runTime",
    "runTimeJson"
})
public class Order implements IJSObject
{

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
    /**
     * xml string of the run time
     * 
     */
    @JsonProperty("runTime")
    @JsonPropertyDescription("xml string of the run time")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "run_time_str", isAttribute = false)
    private String runTime;
    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTimeJson")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private RunTime runTimeJson;

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public void setPriority(String priority) {
        this.priority = priority;
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

    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public String getEndState() {
        return endState;
    }

    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public void setEndState(String endState) {
        this.endState = endState;
    }

    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public String getWebService() {
        return webService;
    }

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
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * xml string of the run time
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "run_time_str", isAttribute = false)
    public String getRunTime() {
        return runTime;
    }

    /**
     * xml string of the run time
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "run_time_str", isAttribute = false)
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTimeJson")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public RunTime getRunTimeJson() {
        return runTimeJson;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTimeJson")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTimeJson(RunTime runTimeJson) {
        this.runTimeJson = runTimeJson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("priority", priority).append("title", title).append("state", state).append("endState", endState).append("webService", webService).append("params", params).append("runTime", runTime).append("runTimeJson", runTimeJson).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(webService).append(runTimeJson).append(endState).append(state).append(runTime).append(priority).append(title).append(params).toHashCode();
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
        return new EqualsBuilder().append(webService, rhs.webService).append(runTimeJson, rhs.runTimeJson).append(endState, rhs.endState).append(state, rhs.state).append(runTime, rhs.runTime).append(priority, rhs.priority).append(title, rhs.title).append(params, rhs.params).isEquals();
    }

}
