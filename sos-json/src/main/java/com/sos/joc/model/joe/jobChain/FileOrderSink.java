
package com.sos.joc.model.joe.jobChain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fileOrderSink
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "file_order_sink")
@JsonPropertyOrder({
    "state",
    "moveTo",
    "remove",
    "delay"
})
public class FileOrderSink {

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JsonPropertyDescription("name of the job chain node")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;
    /**
     * path of the target folder
     * 
     */
    @JsonProperty("moveTo")
    @JsonPropertyDescription("path of the target folder")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    private String moveTo;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("remove")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "remove", isAttribute = true)
    private String remove;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    private Integer delay;

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public String getState() {
        return state;
    }

    /**
     * name of the job chain node
     * (Required)
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    public void setState(String state) {
        this.state = state;
    }

    /**
     * path of the target folder
     * 
     */
    @JsonProperty("moveTo")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    public String getMoveTo() {
        return moveTo;
    }

    /**
     * path of the target folder
     * 
     */
    @JsonProperty("moveTo")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    public void setMoveTo(String moveTo) {
        this.moveTo = moveTo;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("remove")
    @JacksonXmlProperty(localName = "remove", isAttribute = true)
    public String getRemove() {
        return remove;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("remove")
    @JacksonXmlProperty(localName = "remove", isAttribute = true)
    public void setRemove(String remove) {
        this.remove = remove;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public Integer getDelay() {
        return delay;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("state", state).append("moveTo", moveTo).append("remove", remove).append("delay", delay).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).append(delay).append(remove).append(moveTo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileOrderSink) == false) {
            return false;
        }
        FileOrderSink rhs = ((FileOrderSink) other);
        return new EqualsBuilder().append(state, rhs.state).append(delay, rhs.delay).append(remove, rhs.remove).append(moveTo, rhs.moveTo).isEquals();
    }

}
