
package com.sos.joc.model.conditions;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Conditions Events Filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobSchedulerId",
    "session",
    "path",
    "workflow",
    "outConditionId",
    "limit"
})
public class ConditionEventsFilter {

    @JsonProperty("jobSchedulerId")
    private String jobSchedulerId;
    @JsonProperty("session")
    private String session;
    @JsonProperty("path")
    private String path;
    @JsonProperty("workflow")
    private String workflow;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("outConditionId")
    private Long outConditionId;
    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     */
    @JsonProperty("limit")
    private Integer limit = 0;

    /**
     * 
     * @return
     *     The jobSchedulerId
     */
    @JsonProperty("jobSchedulerId")
    public String getJobSchedulerId() {
        return jobSchedulerId;
    }

    /**
     * 
     * @param jobSchedulerId
     *     The jobSchedulerId
     */
    @JsonProperty("jobSchedulerId")
    public void setJobSchedulerId(String jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    /**
     * 
     * @return
     *     The session
     */
    @JsonProperty("session")
    public String getSession() {
        return session;
    }

    /**
     * 
     * @param session
     *     The session
     */
    @JsonProperty("session")
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
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
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The outConditionId
     */
    @JsonProperty("outConditionId")
    public Long getOutConditionId() {
        return outConditionId;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param outConditionId
     *     The outConditionId
     */
    @JsonProperty("outConditionId")
    public void setOutConditionId(Long outConditionId) {
        this.outConditionId = outConditionId;
    }

    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     * @return
     *     The limit
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    /**
     * restricts the number of responsed records; -1=unlimited
     * 
     * @param limit
     *     The limit
     */
    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobSchedulerId).append(session).append(path).append(workflow).append(outConditionId).append(limit).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConditionEventsFilter) == false) {
            return false;
        }
        ConditionEventsFilter rhs = ((ConditionEventsFilter) other);
        return new EqualsBuilder().append(jobSchedulerId, rhs.jobSchedulerId).append(session, rhs.session).append(path, rhs.path).append(workflow, rhs.workflow).append(outConditionId, rhs.outConditionId).append(limit, rhs.limit).isEquals();
    }

}
