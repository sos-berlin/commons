
package com.sos.joc.model.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * auditParams
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "audit_params")
@JsonPropertyOrder({
    "comment",
    "timeSpent",
    "ticketLink"
})
public class AuditParams {

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment", isAttribute = true)
    private String comment;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    @JacksonXmlProperty(localName = "time_spent", isAttribute = true)
    private Integer timeSpent;
    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    @JacksonXmlProperty(localName = "ticket_link", isAttribute = true)
    private String ticketLink;

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment", isAttribute = true)
    public String getComment() {
        return comment;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment", isAttribute = true)
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    @JacksonXmlProperty(localName = "time_spent", isAttribute = true)
    public Integer getTimeSpent() {
        return timeSpent;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    @JacksonXmlProperty(localName = "time_spent", isAttribute = true)
    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    @JacksonXmlProperty(localName = "ticket_link", isAttribute = true)
    public String getTicketLink() {
        return ticketLink;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("ticketLink")
    @JacksonXmlProperty(localName = "ticket_link", isAttribute = true)
    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("comment", comment).append("timeSpent", timeSpent).append("ticketLink", ticketLink).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(comment).append(timeSpent).append(ticketLink).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AuditParams) == false) {
            return false;
        }
        AuditParams rhs = ((AuditParams) other);
        return new EqualsBuilder().append(comment, rhs.comment).append(timeSpent, rhs.timeSpent).append(ticketLink, rhs.ticketLink).isEquals();
    }

}
