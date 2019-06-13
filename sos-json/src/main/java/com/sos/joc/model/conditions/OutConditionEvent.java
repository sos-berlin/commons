
package com.sos.joc.model.conditions;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Out-Condition-Event
 * <p>
 * Out Condition Event
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "event",
    "exists",
    "existsInWorkflow",
    "workflow"
})
public class OutConditionEvent {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    @JsonProperty("event")
    private String event;
    @JsonProperty("exists")
    private Boolean exists;
    @JsonProperty("existsInWorkflow")
    private Boolean existsInWorkflow;
    @JsonProperty("workflow")
    private String workflow;

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
     *     The event
     */
    @JsonProperty("event")
    public String getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The exists
     */
    @JsonProperty("exists")
    public Boolean getExists() {
        return exists;
    }

    /**
     * 
     * @param exists
     *     The exists
     */
    @JsonProperty("exists")
    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    /**
     * 
     * @return
     *     The existsInWorkflow
     */
    @JsonProperty("existsInWorkflow")
    public Boolean getExistsInWorkflow() {
        return existsInWorkflow;
    }

    /**
     * 
     * @param existsInWorkflow
     *     The existsInWorkflow
     */
    @JsonProperty("existsInWorkflow")
    public void setExistsInWorkflow(Boolean existsInWorkflow) {
        this.existsInWorkflow = existsInWorkflow;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(event).append(exists).append(existsInWorkflow).append(workflow).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OutConditionEvent) == false) {
            return false;
        }
        OutConditionEvent rhs = ((OutConditionEvent) other);
        return new EqualsBuilder().append(id, rhs.id).append(event, rhs.event).append(exists, rhs.exists).append(existsInWorkflow, rhs.existsInWorkflow).append(workflow, rhs.workflow).isEquals();
    }

}
