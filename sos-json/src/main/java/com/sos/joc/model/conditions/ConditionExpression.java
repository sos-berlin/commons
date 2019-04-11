
package com.sos.joc.model.conditions;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Expression
 * <p>
 * Expression for Condition
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "expression",
    "validatedExpression",
    "value"
})
public class ConditionExpression {

    @JsonProperty("expression")
    private String expression;
    @JsonProperty("validatedExpression")
    private String validatedExpression;
    @JsonProperty("value")
    private Boolean value;

    /**
     * 
     * @return
     *     The expression
     */
    @JsonProperty("expression")
    public String getExpression() {
        return expression;
    }

    /**
     * 
     * @param expression
     *     The expression
     */
    @JsonProperty("expression")
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * 
     * @return
     *     The validatedExpression
     */
    @JsonProperty("validatedExpression")
    public String getValidatedExpression() {
        return validatedExpression;
    }

    /**
     * 
     * @param validatedExpression
     *     The validatedExpression
     */
    @JsonProperty("validatedExpression")
    public void setValidatedExpression(String validatedExpression) {
        this.validatedExpression = validatedExpression;
    }

    /**
     * 
     * @return
     *     The value
     */
    @JsonProperty("value")
    public Boolean getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    @JsonProperty("value")
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(expression).append(validatedExpression).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConditionExpression) == false) {
            return false;
        }
        ConditionExpression rhs = ((ConditionExpression) other);
        return new EqualsBuilder().append(expression, rhs.expression).append(validatedExpression, rhs.validatedExpression).append(value, rhs.value).isEquals();
    }

}
