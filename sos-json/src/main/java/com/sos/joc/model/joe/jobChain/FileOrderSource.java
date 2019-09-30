
package com.sos.joc.model.joe.jobchain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    private String alertWhenDirectoryMissing;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FileOrderSource() {
    }

    /**
     * 
     * @param regex
     * @param max
     * @param repeat
     * @param delayAfterError
     * @param nextState
     * @param alertWhenDirectoryMissing
     * @param directory
     */
    public FileOrderSource(String directory, String regex, String repeat, Integer delayAfterError, Integer max, String nextState, String alertWhenDirectoryMissing) {
        this.directory = directory;
        this.regex = regex;
        this.repeat = repeat;
        this.delayAfterError = delayAfterError;
        this.max = max;
        this.nextState = nextState;
        this.alertWhenDirectoryMissing = alertWhenDirectoryMissing;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The directory
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
     * @param directory
     *     The directory
     */
    @JsonProperty("directory")
    @JacksonXmlProperty(localName = "directory", isAttribute = true)
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 
     * @return
     *     The regex
     */
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public String getRegex() {
        return regex;
    }

    /**
     * 
     * @param regex
     *     The regex
     */
    @JsonProperty("regex")
    @JacksonXmlProperty(localName = "regex", isAttribute = true)
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * possible values: 'no' or positive number
     * 
     * @return
     *     The repeat
     */
    @JsonProperty("repeat")
    @JacksonXmlProperty(localName = "repeat", isAttribute = true)
    public String getRepeat() {
        return repeat;
    }

    /**
     * possible values: 'no' or positive number
     * 
     * @param repeat
     *     The repeat
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
     * @return
     *     The delayAfterError
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
     * @param delayAfterError
     *     The delayAfterError
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
     * @return
     *     The max
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
     * @param max
     *     The max
     */
    @JsonProperty("max")
    @JacksonXmlProperty(localName = "max", isAttribute = true)
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * 
     * @return
     *     The nextState
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public String getNextState() {
        return nextState;
    }

    /**
     * 
     * @param nextState
     *     The nextState
     */
    @JsonProperty("nextState")
    @JacksonXmlProperty(localName = "next_state", isAttribute = true)
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The alertWhenDirectoryMissing
     */
    @JsonProperty("alertWhenDirectoryMissing")
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    public String getAlertWhenDirectoryMissing() {
        return alertWhenDirectoryMissing;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param alertWhenDirectoryMissing
     *     The alertWhenDirectoryMissing
     */
    @JsonProperty("alertWhenDirectoryMissing")
    @JacksonXmlProperty(localName = "alert_when_directory_missing", isAttribute = true)
    public void setAlertWhenDirectoryMissing(String alertWhenDirectoryMissing) {
        this.alertWhenDirectoryMissing = alertWhenDirectoryMissing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(regex).append(repeat).append(delayAfterError).append(max).append(nextState).append(alertWhenDirectoryMissing).toHashCode();
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
        return new EqualsBuilder().append(directory, rhs.directory).append(regex, rhs.regex).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(max, rhs.max).append(nextState, rhs.nextState).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).isEquals();
    }

}
