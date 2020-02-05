
package com.sos.joc.model.joe.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "order")
@JsonPropertyOrder({
    "priority",
    "title",
    "initialState",
    "endState",
    "params",
    "runTime"
})
public class Order implements IJSObject
{

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    private String priority;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    @JsonProperty("initialState")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String initialState;
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    private String endState;
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
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private RunTime runTime = new RunTime();

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

    @JsonProperty("initialState")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getInitialState() {
        return initialState;
    }

    @JsonProperty("initialState")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setInitialState(String initialState) {
        this.initialState = initialState;
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
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public RunTime getRunTime() {
        return runTime;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(RunTime runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("priority", priority).append("title", title).append("initialState", initialState).append("endState", endState).append("params", params).append("runTime", runTime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(initialState).append(endState).append(runTime).append(priority).append(title).append(params).toHashCode();
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
        return new EqualsBuilder().append(initialState, rhs.initialState).append(endState, rhs.endState).append(runTime, rhs.runTime).append(priority, rhs.priority).append(title, rhs.title).append(params, rhs.params).isEquals();
    }

}
