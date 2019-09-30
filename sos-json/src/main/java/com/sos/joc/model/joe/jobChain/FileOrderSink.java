
package com.sos.joc.model.joe.jobchain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "state", isAttribute = true)
    private String state;
    /**
     * path of the target folder
     * 
     */
    @JsonProperty("moveTo")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    private String moveTo;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("remove")
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
     * No args constructor for use in serialization
     * 
     */
    public FileOrderSink() {
    }

    /**
     * 
     * @param delay
     * @param state
     * @param remove
     * @param moveTo
     */
    public FileOrderSink(String state, String moveTo, String remove, Integer delay) {
        this.state = state;
        this.moveTo = moveTo;
        this.remove = remove;
        this.delay = delay;
    }

    /**
     * name of the job chain node
     * (Required)
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
     * name of the job chain node
     * (Required)
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
     * path of the target folder
     * 
     * @return
     *     The moveTo
     */
    @JsonProperty("moveTo")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    public String getMoveTo() {
        return moveTo;
    }

    /**
     * path of the target folder
     * 
     * @param moveTo
     *     The moveTo
     */
    @JsonProperty("moveTo")
    @JacksonXmlProperty(localName = "move_to", isAttribute = true)
    public void setMoveTo(String moveTo) {
        this.moveTo = moveTo;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The remove
     */
    @JsonProperty("remove")
    @JacksonXmlProperty(localName = "remove", isAttribute = true)
    public String getRemove() {
        return remove;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param remove
     *     The remove
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
     * @return
     *     The delay
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
     * @param delay
     *     The delay
     */
    @JsonProperty("delay")
    @JacksonXmlProperty(localName = "delay", isAttribute = true)
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).append(moveTo).append(remove).append(delay).toHashCode();
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
        return new EqualsBuilder().append(state, rhs.state).append(moveTo, rhs.moveTo).append(remove, rhs.remove).append(delay, rhs.delay).isEquals();
    }

}
