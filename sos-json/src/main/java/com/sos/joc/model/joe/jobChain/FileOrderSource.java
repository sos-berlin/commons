
package com.sos.joc.model.joe.jobChain;

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
 * fileOrderSource
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "file_order_source")
@JsonPropertyOrder({
    "directory",
    "regex",
    "repeat",
    "delayAfterError",
    "max",
    "nextState",
    "alertWhenDirectoryMissing"
})
public class FileOrderSource {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    private String directory;
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    private String regex;
    /**
     * possible values: 'no' or positive number
     * 
     */
    @JsonProperty("repeat")
    @JsonPropertyDescription("possible values: 'no' or positive number")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    private String repeat;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delayAfterError")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = true)
    private Integer delayAfterError;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("max")
    @JacksonXmlProperty(localName = "max", isAttribute = true)
    private Integer max;
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    private String nextState;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("alertWhenDirectoryMissing")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    private String alertWhenDirectoryMissing;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public String getDirectory() {
        return directory;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public String getRegex() {
        return regex;
    }

    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * possible values: 'no' or positive number
     * 
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    public String getRepeat() {
        return repeat;
    }

    /**
     * possible values: 'no' or positive number
     * 
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delayAfterError")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = true)
    public Integer getDelayAfterError() {
        return delayAfterError;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delayAfterError")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = true)
    public void setDelayAfterError(Integer delayAfterError) {
        this.delayAfterError = delayAfterError;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("max")
    @JacksonXmlProperty(localName = "max", isAttribute = true)
    public Integer getMax() {
        return max;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("max")
    @JacksonXmlProperty(localName = "max", isAttribute = true)
    public void setMax(Integer max) {
        this.max = max;
    }

    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public String getNextState() {
        return nextState;
    }

    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("alertWhenDirectoryMissing")
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    public String getAlertWhenDirectoryMissing() {
        return alertWhenDirectoryMissing;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("alertWhenDirectoryMissing")
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    public void setAlertWhenDirectoryMissing(String alertWhenDirectoryMissing) {
        this.alertWhenDirectoryMissing = alertWhenDirectoryMissing;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("directory", directory).append("regex", regex).append("repeat", repeat).append("delayAfterError", delayAfterError).append("max", max).append("nextState", nextState).append("alertWhenDirectoryMissing", alertWhenDirectoryMissing).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(regex).append(max).append(repeat).append(delayAfterError).append(nextState).append(alertWhenDirectoryMissing).append(directory).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileOrderSource) == false) {
            return false;
        }
        FileOrderSource rhs = ((FileOrderSource) other);
        return new EqualsBuilder().append(regex, rhs.regex).append(max, rhs.max).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(nextState, rhs.nextState).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).append(directory, rhs.directory).isEquals();
    }

}
