
package com.sos.joc.model.joe.schedule;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * runTime
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "run_time")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "schedule"
})
public class RunTime
    extends AbstractSchedule
    implements IJSObject
{

    /**
     * path to a schedule
     * 
     */
    @JsonProperty("schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    private String schedule;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RunTime() {
    }

    /**
     * 
     * @param schedule
     */
    public RunTime(String schedule) {
        this.schedule = schedule;
    }

    /**
     * path to a schedule
     * 
     * @return
     *     The schedule
     */
    @JsonProperty("schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    public String getSchedule() {
        return schedule;
    }

    /**
     * path to a schedule
     * 
     * @param schedule
     *     The schedule
     */
    @JsonProperty("schedule")
    @JacksonXmlProperty(localName = "schedule", isAttribute = true)
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(schedule).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RunTime) == false) {
            return false;
        }
        RunTime rhs = ((RunTime) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(schedule, rhs.schedule).isEquals();
    }

}
