
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "login")
@JsonPropertyOrder({
    "user",
    "password"
})
public class Login {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    @JacksonXmlProperty(localName = "user", isAttribute = true)
    private String user;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("password")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "password.plain", isAttribute = false)
    private String password;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Login() {
    }

    /**
     * 
     * @param password
     * @param user
     */
    public Login(String user, String password) {
        super();
        this.user = user;
        this.password = password;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    @JacksonXmlProperty(localName = "user", isAttribute = true)
    public String getUser() {
        return user;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    @JacksonXmlProperty(localName = "user", isAttribute = true)
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("password")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "password.plain", isAttribute = false)
    public String getPassword() {
        return password;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("password")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "password.plain", isAttribute = false)
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", user).append("password", password).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(user).append(password).toHashCode();
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
        return new EqualsBuilder().append(user, rhs.user).append(password, rhs.password).isEquals();
    }

}
