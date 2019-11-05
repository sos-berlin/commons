
package com.sos.joc.model.xmleditor.delete;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor delete other draft configuration answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deleted",
    "found"
})
public class DeleteOtherDraftAnswer {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("deleted")
    private Date deleted;
    @JsonProperty("found")
    private Boolean found;

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The deleted
     */
    @JsonProperty("deleted")
    public Date getDeleted() {
        return deleted;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param deleted
     *     The deleted
     */
    @JsonProperty("deleted")
    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    /**
     * 
     * @return
     *     The found
     */
    @JsonProperty("found")
    public Boolean getFound() {
        return found;
    }

    /**
     * 
     * @param found
     *     The found
     */
    @JsonProperty("found")
    public void setFound(Boolean found) {
        this.found = found;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deleted).append(found).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeleteOtherDraftAnswer) == false) {
            return false;
        }
        DeleteOtherDraftAnswer rhs = ((DeleteOtherDraftAnswer) other);
        return new EqualsBuilder().append(deleted, rhs.deleted).append(found, rhs.found).isEquals();
    }

}
