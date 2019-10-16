
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job settings
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "settings")
@JsonPropertyOrder({
    "mailOnError",
    "mailOnWarning",
    "mailOnSuccess",
    "mailOnProcess",
    "mailOnDelayAfterError",
    "logMailTo",
    "logMailCc",
    "logMailBcc",
    "logLevel",
    "history",
    "historyOnProcess",
    "historyWithLog"
})
public class Settings {

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnError")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "mail_on_error", isAttribute = false)
    private String mailOnError;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnWarning")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "mail_on_warning", isAttribute = false)
    private String mailOnWarning;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnSuccess")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "mail_on_success", isAttribute = false)
    private String mailOnSuccess;
    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("mailOnProcess")
    @JsonPropertyDescription("possible values: yes, no, true, false, non-negative integer")
    @JacksonXmlProperty(localName = "mail_on_process", isAttribute = false)
    private String mailOnProcess;
    /**
     * possible values: all, first_only, last_only, first_and_last_only
     * 
     */
    @JsonProperty("mailOnDelayAfterError")
    @JsonPropertyDescription("possible values: all, first_only, last_only, first_and_last_only")
    @JacksonXmlProperty(localName = "mail_on_delay_after_error", isAttribute = false)
    private String mailOnDelayAfterError;
    @JsonProperty("logMailTo")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_to", isAttribute = false)
    private String logMailTo;
    @JsonProperty("logMailCc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_cc", isAttribute = false)
    private String logMailCc;
    @JsonProperty("logMailBcc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_bcc", isAttribute = false)
    private String logMailBcc;
    /**
     * possible values: error, warn, info, debug, debug[1-9]
     * 
     */
    @JsonProperty("logLevel")
    @JsonPropertyDescription("possible values: error, warn, info, debug, debug[1-9]")
    @JacksonXmlProperty(localName = "log_level", isAttribute = false)
    private String logLevel;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("history")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "history", isAttribute = false)
    private String history;
    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("historyOnProcess")
    @JsonPropertyDescription("possible values: yes, no, true, false, non-negative integer")
    @JacksonXmlProperty(localName = "history_on_process", isAttribute = false)
    private String historyOnProcess;
    /**
     * possible values: yes, no, gzip
     * 
     */
    @JsonProperty("historyWithLog")
    @JsonPropertyDescription("possible values: yes, no, gzip")
    @JacksonXmlProperty(localName = "history_with_log", isAttribute = false)
    private String historyWithLog;

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnError")
    @JacksonXmlProperty(localName = "mail_on_error", isAttribute = false)
    public String getMailOnError() {
        return mailOnError;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnError")
    @JacksonXmlProperty(localName = "mail_on_error", isAttribute = false)
    public void setMailOnError(String mailOnError) {
        this.mailOnError = mailOnError;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnWarning")
    @JacksonXmlProperty(localName = "mail_on_warning", isAttribute = false)
    public String getMailOnWarning() {
        return mailOnWarning;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnWarning")
    @JacksonXmlProperty(localName = "mail_on_warning", isAttribute = false)
    public void setMailOnWarning(String mailOnWarning) {
        this.mailOnWarning = mailOnWarning;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnSuccess")
    @JacksonXmlProperty(localName = "mail_on_success", isAttribute = false)
    public String getMailOnSuccess() {
        return mailOnSuccess;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("mailOnSuccess")
    @JacksonXmlProperty(localName = "mail_on_success", isAttribute = false)
    public void setMailOnSuccess(String mailOnSuccess) {
        this.mailOnSuccess = mailOnSuccess;
    }

    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("mailOnProcess")
    @JacksonXmlProperty(localName = "mail_on_process", isAttribute = false)
    public String getMailOnProcess() {
        return mailOnProcess;
    }

    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("mailOnProcess")
    @JacksonXmlProperty(localName = "mail_on_process", isAttribute = false)
    public void setMailOnProcess(String mailOnProcess) {
        this.mailOnProcess = mailOnProcess;
    }

    /**
     * possible values: all, first_only, last_only, first_and_last_only
     * 
     */
    @JsonProperty("mailOnDelayAfterError")
    @JacksonXmlProperty(localName = "mail_on_delay_after_error", isAttribute = false)
    public String getMailOnDelayAfterError() {
        return mailOnDelayAfterError;
    }

    /**
     * possible values: all, first_only, last_only, first_and_last_only
     * 
     */
    @JsonProperty("mailOnDelayAfterError")
    @JacksonXmlProperty(localName = "mail_on_delay_after_error", isAttribute = false)
    public void setMailOnDelayAfterError(String mailOnDelayAfterError) {
        this.mailOnDelayAfterError = mailOnDelayAfterError;
    }

    @JsonProperty("logMailTo")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_to", isAttribute = false)
    public String getLogMailTo() {
        return logMailTo;
    }

    @JsonProperty("logMailTo")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_to", isAttribute = false)
    public void setLogMailTo(String logMailTo) {
        this.logMailTo = logMailTo;
    }

    @JsonProperty("logMailCc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_cc", isAttribute = false)
    public String getLogMailCc() {
        return logMailCc;
    }

    @JsonProperty("logMailCc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_cc", isAttribute = false)
    public void setLogMailCc(String logMailCc) {
        this.logMailCc = logMailCc;
    }

    @JsonProperty("logMailBcc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_bcc", isAttribute = false)
    public String getLogMailBcc() {
        return logMailBcc;
    }

    @JsonProperty("logMailBcc")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "log_mail_bcc", isAttribute = false)
    public void setLogMailBcc(String logMailBcc) {
        this.logMailBcc = logMailBcc;
    }

    /**
     * possible values: error, warn, info, debug, debug[1-9]
     * 
     */
    @JsonProperty("logLevel")
    @JacksonXmlProperty(localName = "log_level", isAttribute = false)
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * possible values: error, warn, info, debug, debug[1-9]
     * 
     */
    @JsonProperty("logLevel")
    @JacksonXmlProperty(localName = "log_level", isAttribute = false)
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("history")
    @JacksonXmlProperty(localName = "history", isAttribute = false)
    public String getHistory() {
        return history;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("history")
    @JacksonXmlProperty(localName = "history", isAttribute = false)
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("historyOnProcess")
    @JacksonXmlProperty(localName = "history_on_process", isAttribute = false)
    public String getHistoryOnProcess() {
        return historyOnProcess;
    }

    /**
     * possible values: yes, no, true, false, non-negative integer
     * 
     */
    @JsonProperty("historyOnProcess")
    @JacksonXmlProperty(localName = "history_on_process", isAttribute = false)
    public void setHistoryOnProcess(String historyOnProcess) {
        this.historyOnProcess = historyOnProcess;
    }

    /**
     * possible values: yes, no, gzip
     * 
     */
    @JsonProperty("historyWithLog")
    @JacksonXmlProperty(localName = "history_with_log", isAttribute = false)
    public String getHistoryWithLog() {
        return historyWithLog;
    }

    /**
     * possible values: yes, no, gzip
     * 
     */
    @JsonProperty("historyWithLog")
    @JacksonXmlProperty(localName = "history_with_log", isAttribute = false)
    public void setHistoryWithLog(String historyWithLog) {
        this.historyWithLog = historyWithLog;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mailOnError", mailOnError).append("mailOnWarning", mailOnWarning).append("mailOnSuccess", mailOnSuccess).append("mailOnProcess", mailOnProcess).append("mailOnDelayAfterError", mailOnDelayAfterError).append("logMailTo", logMailTo).append("logMailCc", logMailCc).append("logMailBcc", logMailBcc).append("logLevel", logLevel).append("history", history).append("historyOnProcess", historyOnProcess).append("historyWithLog", historyWithLog).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mailOnSuccess).append(mailOnWarning).append(logMailTo).append(logMailBcc).append(historyOnProcess).append(historyWithLog).append(logMailCc).append(history).append(logLevel).append(mailOnError).append(mailOnDelayAfterError).append(mailOnProcess).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Settings) == false) {
            return false;
        }
        Settings rhs = ((Settings) other);
        return new EqualsBuilder().append(mailOnSuccess, rhs.mailOnSuccess).append(mailOnWarning, rhs.mailOnWarning).append(logMailTo, rhs.logMailTo).append(logMailBcc, rhs.logMailBcc).append(historyOnProcess, rhs.historyOnProcess).append(historyWithLog, rhs.historyWithLog).append(logMailCc, rhs.logMailCc).append(history, rhs.history).append(logLevel, rhs.logLevel).append(mailOnError, rhs.mailOnError).append(mailOnDelayAfterError, rhs.mailOnDelayAfterError).append(mailOnProcess, rhs.mailOnProcess).isEquals();
    }

}
