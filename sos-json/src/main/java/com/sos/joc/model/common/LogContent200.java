
package com.sos.joc.model.common;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * log content with delivery date
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deliveryDate",
    "surveyDate",
    "log"
})
public class LogContent200 {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JsonPropertyDescription("Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "deliveryDate")
    private Date deliveryDate;
    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    @JsonPropertyDescription("Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ")
    @JacksonXmlProperty(localName = "surveyDate")
    private Date surveyDate;
    /**
     * log content
     * <p>
     * The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.
     * (Required)
     * 
     */
    @JsonProperty("log")
    @JsonPropertyDescription("The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.")
    @JacksonXmlProperty(localName = "log")
    private LogContent log;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    @JacksonXmlProperty(localName = "surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    @JacksonXmlProperty(localName = "surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * log content
     * <p>
     * The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.
     * (Required)
     * 
     */
    @JsonProperty("log")
    @JacksonXmlProperty(localName = "log")
    public LogContent getLog() {
        return log;
    }

    /**
     * log content
     * <p>
     * The parameter 'mime' can specify if the content is plain or html. Either 'plain' or 'html' is required. 'plain' is default.
     * (Required)
     * 
     */
    @JsonProperty("log")
    @JacksonXmlProperty(localName = "log")
    public void setLog(LogContent log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryDate", deliveryDate).append("surveyDate", surveyDate).append("log", log).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(surveyDate).append(log).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LogContent200) == false) {
            return false;
        }
        LogContent200 rhs = ((LogContent200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(surveyDate, rhs.surveyDate).append(log, rhs.log).isEquals();
    }

}
