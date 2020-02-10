
package com.sos.joc.model.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * login
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "defaultProfileAccount",
    "enableRememberMe",
    "customLogo"
})
public class Login {

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("defaultProfileAccount")
    private String defaultProfileAccount;
    @JsonProperty("enableRememberMe")
    private Boolean enableRememberMe;
    /**
     * loginLogo
     * <p>
     * 
     * 
     */
    @JsonProperty("customLogo")
    private LoginLogo customLogo;

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("defaultProfileAccount")
    public String getDefaultProfileAccount() {
        return defaultProfileAccount;
    }

    /**
     * string without < and >
     * <p>
     * 
     * 
     */
    @JsonProperty("defaultProfileAccount")
    public void setDefaultProfileAccount(String defaultProfileAccount) {
        this.defaultProfileAccount = defaultProfileAccount;
    }

    @JsonProperty("enableRememberMe")
    public Boolean getEnableRememberMe() {
        return enableRememberMe;
    }

    @JsonProperty("enableRememberMe")
    public void setEnableRememberMe(Boolean enableRememberMe) {
        this.enableRememberMe = enableRememberMe;
    }

    /**
     * loginLogo
     * <p>
     * 
     * 
     */
    @JsonProperty("customLogo")
    public LoginLogo getCustomLogo() {
        return customLogo;
    }

    /**
     * loginLogo
     * <p>
     * 
     * 
     */
    @JsonProperty("customLogo")
    public void setCustomLogo(LoginLogo customLogo) {
        this.customLogo = customLogo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("defaultProfileAccount", defaultProfileAccount).append("enableRemeberMe", enableRememberMe).append("customLogo", customLogo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(enableRememberMe).append(customLogo).append(defaultProfileAccount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Login) == false) {
            return false;
        }
        Login rhs = ((Login) other);
        return new EqualsBuilder().append(enableRememberMe, rhs.enableRememberMe).append(customLogo, rhs.customLogo).append(defaultProfileAccount, rhs.defaultProfileAccount).isEquals();
    }

}
