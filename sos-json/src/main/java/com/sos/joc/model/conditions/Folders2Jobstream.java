
package com.sos.joc.model.conditions;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * folders2Jobstream
 * <p>
 * List of all In Conditions
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobStream",
    "folders"
})
public class Folders2Jobstream {

    @JsonProperty("jobStream")
    private String jobStream;
    @JsonProperty("folders")
    private List<String> folders = new ArrayList<String>();

    /**
     * 
     * @return
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public String getJobStream() {
        return jobStream;
    }

    /**
     * 
     * @param jobStream
     *     The jobStream
     */
    @JsonProperty("jobStream")
    public void setJobStream(String jobStream) {
        this.jobStream = jobStream;
    }

    /**
     * 
     * @return
     *     The folders
     */
    @JsonProperty("folders")
    public List<String> getFolders() {
        return folders;
    }

    /**
     * 
     * @param folders
     *     The folders
     */
    @JsonProperty("folders")
    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobStream).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Folders2Jobstream) == false) {
            return false;
        }
        Folders2Jobstream rhs = ((Folders2Jobstream) other);
        return new EqualsBuilder().append(jobStream, rhs.jobStream).append(folders, rhs.folders).isEquals();
    }

}
