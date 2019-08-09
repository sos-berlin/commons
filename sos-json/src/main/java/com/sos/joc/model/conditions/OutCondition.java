
package com.sos.joc.model.conditions;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Out-Condition
 * <p>
 * Out Condition
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "jobStream",
    "conditionExpression",
    "outconditionEvents"
})
public class OutCondition {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    @JsonProperty("jobStream")
    private String jobStream;
    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
     */
    @JsonProperty("conditionExpression")
    private ConditionExpression conditionExpression;
    @JsonProperty("outconditionEvents")
    private List<OutConditionEvent> outconditionEvents = new ArrayList<OutConditionEvent>();

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * 
     * @param jobStream
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
     * @return
     *     The conditionExpression
     */
    @JsonProperty("conditionExpression")
    public ConditionExpression getConditionExpression() {
        return conditionExpression;
    }

    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
     * @param conditionExpression
     *     The conditionExpression
     */
    @JsonProperty("conditionExpression")
    public void setConditionExpression(ConditionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    /**
     * 
     * @return
     *     The outconditionEvents
     */
    @JsonProperty("outconditionEvents")
    public List<OutConditionEvent> getOutconditionEvents() {
        return outconditionEvents;
    }

    /**
     * 
     * @param outconditionEvents
     *     The outconditionEvents
     */
    @JsonProperty("outconditionEvents")
    public void setOutconditionEvents(List<OutConditionEvent> outconditionEvents) {
        this.outconditionEvents = outconditionEvents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(jobStream).append(conditionExpression).append(outconditionEvents).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutCondition) == false) {
            return false;
        }
        OutCondition rhs = ((OutCondition) other);
        return new EqualsBuilder().append(id, rhs.id).append(jobStream, rhs.jobStream).append(conditionExpression, rhs.conditionExpression).append(outconditionEvents, rhs.outconditionEvents).isEquals();
    }

}
