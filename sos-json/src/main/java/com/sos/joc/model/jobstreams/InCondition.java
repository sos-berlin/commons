
package com.sos.joc.model.jobstreams;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
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
@JsonPropertyOrder({
    "id",
    "jobStream",
    "consumed",
    "markExpression",
    "skipOutCondition",
    "conditionExpression",
    "nexPeriod",
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
    @JsonProperty("markExpression")
    private Boolean markExpression;
    @JsonProperty("skipOutCondition")
    private Boolean skipOutCondition;
    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
     */
    @JsonProperty("conditionExpression")
    @JsonPropertyDescription("Expression for Condition")
    private ConditionExpression conditionExpression;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("nexPeriod")
    @JsonPropertyDescription("ISO date YYYY-MM-DD")
    private String nexPeriod;
    @JsonProperty("inconditionCommands")
    private List<InConditionCommand> inconditionCommands = new ArrayList<InConditionCommand>();
    @JsonProperty("outconditions")
    private List<JobstreamConditions> outconditions = new ArrayList<JobstreamConditions>();

    /**
     * non negative long
     * <p>
     * 
     * 
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
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    @JsonProperty("consumed")
    public Boolean getConsumed() {
        return consumed;
    }

    @JsonProperty("consumed")
    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    @JsonProperty("markExpression")
    public Boolean getMarkExpression() {
        return markExpression;
    }

    @JsonProperty("markExpression")
    public void setMarkExpression(Boolean markExpression) {
        this.markExpression = markExpression;
    }

    @JsonProperty("skipOutCondition")
    public Boolean getSkipOutCondition() {
        return skipOutCondition;
    }

    @JsonProperty("skipOutCondition")
    public void setSkipOutCondition(Boolean skipOutCondition) {
        this.skipOutCondition = skipOutCondition;
    }

    /**
     * Expression
     * <p>
     * Expression for Condition
     * 
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
     */
    @JsonProperty("conditionExpression")
    public void setConditionExpression(ConditionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("nexPeriod")
    public String getNexPeriod() {
        return nexPeriod;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("nexPeriod")
    public void setNexPeriod(String nexPeriod) {
        this.nexPeriod = nexPeriod;
    }

    @JsonProperty("inconditionCommands")
    public List<InConditionCommand> getInconditionCommands() {
        return inconditionCommands;
    }

    @JsonProperty("inconditionCommands")
    public void setInconditionCommands(List<InConditionCommand> inconditionCommands) {
        this.inconditionCommands = inconditionCommands;
    }

    @JsonProperty("outconditions")
    public List<JobstreamConditions> getOutconditions() {
        return outconditions;
    }

    @JsonProperty("outconditions")
    public void setOutconditions(List<JobstreamConditions> outconditions) {
        this.outconditions = outconditions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("jobStream", jobStream).append("consumed", consumed).append("markExpression", markExpression).append("skipOutCondition", skipOutCondition).append("conditionExpression", conditionExpression).append("nexPeriod", nexPeriod).append("inconditionCommands", inconditionCommands).append("outconditions", outconditions).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(consumed).append(nexPeriod).append(markExpression).append(conditionExpression).append(outconditions).append(jobStream).append(id).append(inconditionCommands).append(skipOutCondition).toHashCode();
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
        return new EqualsBuilder().append(consumed, rhs.consumed).append(nexPeriod, rhs.nexPeriod).append(markExpression, rhs.markExpression).append(conditionExpression, rhs.conditionExpression).append(outconditions, rhs.outconditions).append(jobStream, rhs.jobStream).append(id, rhs.id).append(inconditionCommands, rhs.inconditionCommands).append(skipOutCondition, rhs.skipOutCondition).isEquals();
    }

}
