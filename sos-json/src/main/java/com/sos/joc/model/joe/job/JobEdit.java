
package com.sos.joc.model.joe.job;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.audit.AuditParams;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit job configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job_edit")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "configurationDate",
    "jobschedulerId",
    "job",
    "configuration",
    "auditLog"
})
public class JobEdit {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
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
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    private String job;
    /**
     * job without name, visible, temporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet attributes and TODO: commands, run_time Element
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    private Job configuration;
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
     * @return
     *     The deliveryDate
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
     * @param deliveryDate
     *     The deliveryDate
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
     * @return
     *     The configurationDate
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
     * @param configurationDate
     *     The configurationDate
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
     * @return
     *     The jobschedulerId
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
     * @param jobschedulerId
     *     The jobschedulerId
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
     * @return
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * job without name, visible, temporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet attributes and TODO: commands, run_time Element
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public Job getConfiguration() {
        return configuration;
    }

    /**
     * job without name, visible, temporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet attributes and TODO: commands, run_time Element
     * <p>
     * 
     * (Required)
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public void setConfiguration(Job configuration) {
        this.configuration = configuration;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @return
     *     The auditLog
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
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(configurationDate).append(jobschedulerId).append(job).append(configuration).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobEdit) == false) {
            return false;
        }
        JobEdit rhs = ((JobEdit) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(configurationDate, rhs.configurationDate).append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(configuration, rhs.configuration).append(auditLog, rhs.auditLog).isEquals();
    }

}
