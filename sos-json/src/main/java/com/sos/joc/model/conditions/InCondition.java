
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
    "workflow",
    "conditionExpression",
    "inconditionCommands"
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
    @JsonProperty("workflow")
    private String workflow;
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
     *     The workflow
     */
    @JsonProperty("workflow")
    public String getWorkflow() {
        return workflow;
    }

    /**
     * 
     * @param workflow
     *     The workflow
     */
    @JsonProperty("workflow")
    public void setWorkflow(String workflow) {
        this.workflow = workflow;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(workflow).append(conditionExpression).append(inconditionCommands).toHashCode();
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
        return new EqualsBuilder().append(id, rhs.id).append(workflow, rhs.workflow).append(conditionExpression, rhs.conditionExpression).append(inconditionCommands, rhs.inconditionCommands).isEquals();
    }

}
