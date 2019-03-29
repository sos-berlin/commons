
package com.sos.joc.model.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChain state
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "_text",
    "manually"
})
public class OrderState {

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * order state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private OrderStateText _text;
    @JsonProperty("manually")
    private Boolean manually;

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * order state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public OrderStateText get_text() {
        return _text;
    }

    /**
     * order state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(OrderStateText _text) {
        this._text = _text;
    }

    /**
     * 
     * @return
     *     The manually
     */
    @JsonProperty("manually")
    public Boolean getManually() {
        return manually;
    }

    /**
     * 
     * @param manually
     *     The manually
     */
    @JsonProperty("manually")
    public void setManually(Boolean manually) {
        this.manually = manually;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(_text).append(manually).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderState) == false) {
            return false;
        }
        OrderState rhs = ((OrderState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).append(manually, rhs.manually).isEquals();
    }

}
