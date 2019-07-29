
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
 * In-Condition
 * <p>
 * In Condition
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "jobStream",
    "consumed",
    "conditionExpression",
    "inconditionCommands",
    "outconditions"
})
public class InCondition {

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
    @JsonProperty("consumed")
    private Boolean consumed;
    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
     */
    @JsonProperty("conditionExpression")
    private ConditionExpression conditionExpression;
    @JsonProperty("inconditionCommands")
    private List<InConditionCommand> inconditionCommands = new ArrayList<InConditionCommand>();
    @JsonProperty("outconditions")
    private List<JobOutCondition> outconditions = new ArrayList<JobOutCondition>();

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
     * 
     * @return
     *     The consumed
     */
    @JsonProperty("consumed")
    public Boolean getConsumed() {
        return consumed;
    }

    /**
     * 
     * @param consumed
     *     The consumed
     */
    @JsonProperty("consumed")
    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
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
     *     The inconditionCommands
     */
    @JsonProperty("inconditionCommands")
    public List<InConditionCommand> getInconditionCommands() {
        return inconditionCommands;
    }

    /**
     * 
     * @param inconditionCommands
     *     The inconditionCommands
     */
    @JsonProperty("inconditionCommands")
    public void setInconditionCommands(List<InConditionCommand> inconditionCommands) {
        this.inconditionCommands = inconditionCommands;
    }

    /**
     * 
     * @return
     *     The outconditions
     */
    @JsonProperty("outconditions")
    public List<JobOutCondition> getOutconditions() {
        return outconditions;
    }

    /**
     * 
     * @param outconditions
     *     The outconditions
     */
    @JsonProperty("outconditions")
    public void setOutconditions(List<JobOutCondition> outconditions) {
        this.outconditions = outconditions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(jobStream).append(consumed).append(conditionExpression).append(inconditionCommands).append(outconditions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof InCondition) == false) {
            return false;
        }
        InCondition rhs = ((InCondition) other);
        return new EqualsBuilder().append(id, rhs.id).append(jobStream, rhs.jobStream).append(consumed, rhs.consumed).append(conditionExpression, rhs.conditionExpression).append(inconditionCommands, rhs.inconditionCommands).append(outconditions, rhs.outconditions).isEquals();
    }

}
