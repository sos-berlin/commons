
package com.sos.joc.model.joe.job;

import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "password", isAttribute = false)
    private String password;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The user
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
     * @param user
     *     The user
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
     * @return
     *     The password
     */
    @JsonProperty("password")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "password", isAttribute = false)
    public String getPassword() {
        return password;
    }

    /**
     * 
     * (Required)
     * 
     * @param password
     *     The password
     */
    @JsonProperty("password")
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "password", isAttribute = false)
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
