
package com.sos.joc.model.joe.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * folder item
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "folder_item")
@JsonPropertyOrder({
    "name",
    "deployed",
    "title",
    "processClass",
    "isOrderJob",
    "priority",
    "initialState",
    "endState",
    "maxProcesses",
    "maxNonExclusive",
    "validFrom",
    "validTo",
    "substitute"
})
public class FolderItem {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    private Boolean deployed = false;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    /**
     * for jobs and job chains
     * 
     */
    @JsonProperty("processClass")
    @JsonPropertyDescription("for jobs and job chains")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private String processClass;
    /**
     * for jobs
     * 
     */
    @JsonProperty("isOrderJob")
    @JsonPropertyDescription("for jobs")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    private Boolean isOrderJob;
    /**
     * for orders
     * 
     */
    @JsonProperty("priority")
    @JsonPropertyDescription("for orders")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    private String priority;
    /**
     * for orders
     * 
     */
    @JsonProperty("initialState")
    @JsonPropertyDescription("for orders")
    @JacksonXmlProperty(localName = "initial_state", isAttribute = true)
    private String initialState;
    /**
     * for orders
     * 
     */
    @JsonProperty("endState")
    @JsonPropertyDescription("for orders")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    private String endState;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    private Integer maxProcesses;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    private Integer maxNonExclusive;
    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validFrom")
    @JsonPropertyDescription("for schedule: yyyy-mm-dd HH:MM[:SS]")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    private String validFrom;
    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validTo")
    @JsonPropertyDescription("for schedule: yyyy-mm-dd HH:MM[:SS]")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    private String validTo;
    /**
     * for schedule: path to another schedule
     * 
     */
    @JsonProperty("substitute")
    @JsonPropertyDescription("for schedule: path to another schedule")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    private String substitute;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    @JacksonXmlProperty(localName = "deployed", isAttribute = true)
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * for jobs and job chains
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * for jobs and job chains
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * for jobs
     * 
     */
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    /**
     * for jobs
     * 
     */
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "is_order_job", isAttribute = true)
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public String getPriority() {
        return priority;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("initialState")
    @JacksonXmlProperty(localName = "initial_state", isAttribute = true)
    public String getInitialState() {
        return initialState;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("initialState")
    @JacksonXmlProperty(localName = "initial_state", isAttribute = true)
    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public String getEndState() {
        return endState;
    }

    /**
     * for orders
     * 
     */
    @JsonProperty("endState")
    @JacksonXmlProperty(localName = "end_state", isAttribute = true)
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    public Integer getMaxProcesses() {
        return maxProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxProcesses")
    @JacksonXmlProperty(localName = "max_processes", isAttribute = true)
    public void setMaxProcesses(Integer maxProcesses) {
        this.maxProcesses = maxProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    public Integer getMaxNonExclusive() {
        return maxNonExclusive;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxNonExclusive")
    @JacksonXmlProperty(localName = "max_non_exclusive", isAttribute = true)
    public void setMaxNonExclusive(Integer maxNonExclusive) {
        this.maxNonExclusive = maxNonExclusive;
    }

    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validFrom")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validFrom")
    @JacksonXmlProperty(localName = "valid_from", isAttribute = true)
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validTo")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    public String getValidTo() {
        return validTo;
    }

    /**
     * for schedule: yyyy-mm-dd HH:MM[:SS]
     * 
     */
    @JsonProperty("validTo")
    @JacksonXmlProperty(localName = "valid_to", isAttribute = true)
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    /**
     * for schedule: path to another schedule
     * 
     */
    @JsonProperty("substitute")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    public String getSubstitute() {
        return substitute;
    }

    /**
     * for schedule: path to another schedule
     * 
     */
    @JsonProperty("substitute")
    @JacksonXmlProperty(localName = "substitute", isAttribute = true)
    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("deployed", deployed).append("title", title).append("processClass", processClass).append("isOrderJob", isOrderJob).append("priority", priority).append("initialState", initialState).append("endState", endState).append("maxProcesses", maxProcesses).append("maxNonExclusive", maxNonExclusive).append("validFrom", validFrom).append("validTo", validTo).append("substitute", substitute).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FolderItem) == false) {
            return false;
        }
        FolderItem rhs = ((FolderItem) other);
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

}
