
package com.sos.joc.model.yade.xmleditor.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * xmleditor yade profile
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deployed",
    "profile"
})
public class Profile {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    private Boolean deployed;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("profile")
    private String profile;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The deployed
     */
    @JsonProperty("deployed")
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * (Required)
     * 
     * @param deployed
     *     The deployed
     */
    @JsonProperty("deployed")
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The profile
     */
    @JsonProperty("profile")
    public String getProfile() {
        return profile;
    }

    /**
     * 
     * (Required)
     * 
     * @param profile
     *     The profile
     */
    @JsonProperty("profile")
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deployed).append(profile).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Profile) == false) {
            return false;
        }
        Profile rhs = ((Profile) other);
        return new EqualsBuilder().append(deployed, rhs.deployed).append(profile, rhs.profile).isEquals();
    }

}
