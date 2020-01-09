
package com.sos.joc.model.joe.wizard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobs filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "job_filter")
@JsonPropertyOrder({
    "jobschedulerId",
    "docPath"
})
public class JobFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    private String docPath;

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    public String getDocPath() {
        return docPath;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("docPath")
    @JacksonXmlProperty(localName = "doc_path", isAttribute = true)
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("docPath", docPath).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(docPath).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobFilter) == false) {
            return false;
        }
        JobFilter rhs = ((JobFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(docPath, rhs.docPath).isEquals();
    }

}
