
package com.sos.joc.model.processClass;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.joe.processclass.ProcessClass;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit processClass configuration
 * <p>
 * old version from 1.13.0
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "configuration_edit")
@JsonPropertyOrder({
    "deliveryDate",
    "configurationDate",
    "jobschedulerId",
    "processClass",
    "configuration",
    "auditLog"
})
public class ConfigurationEdit {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    @JsonPropertyDescription("Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    private Date configurationDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("processClass")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private String processClass;
    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    private ProcessClass configuration;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    private AuditParams auditLog;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    public ProcessClass getConfiguration() {
        return configuration;
    }

    /**
     * processClass without name, replace, spooler_id attributes
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "process_class", isAttribute = false)
    public void setConfiguration(ProcessClass configuration) {
        this.configuration = configuration;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("configurationDate", configurationDate).append("jobschedulerId", jobschedulerId).append("processClass", processClass).append("configuration", configuration).append("auditLog", auditLog).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(configurationDate).append(auditLog).append(configuration).append(processClass).append(deliveryDate).append(jobschedulerId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigurationEdit) == false) {
            return false;
        }
        ConfigurationEdit rhs = ((ConfigurationEdit) other);
        return new EqualsBuilder().append(configurationDate, rhs.configurationDate).append(auditLog, rhs.auditLog).append(configuration, rhs.configuration).append(processClass, rhs.processClass).append(deliveryDate, rhs.deliveryDate).append(jobschedulerId, rhs.jobschedulerId).isEquals();
    }

}
