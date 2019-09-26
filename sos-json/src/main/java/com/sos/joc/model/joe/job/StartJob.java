
package com.sos.joc.model.joe.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.Params;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * start job command
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "start_job")
@JsonPropertyOrder({
    "job",
    "at",
    "after",
    "webService",
    "force",
    "params",
    "environment"
})
public class StartJob {

    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    private String job;
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    private String at = "now";
    @JsonProperty("after")
    @JacksonXmlProperty(localName = "after", isAttribute = true)
    private String after;
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    private String webService;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("force")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "force", isAttribute = true)
    private String force;
    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private Params params;
    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    private EnviromentVariables environment;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StartJob() {
    }

    /**
     * 
     * @param environment
     * @param at
     * @param webService
     * @param force
     * @param after
     * @param job
     * @param params
     */
    public StartJob(String job, String at, String after, String webService, String force, Params params, EnviromentVariables environment) {
        super();
        this.job = job;
        this.at = at;
        this.after = after;
        this.webService = webService;
        this.force = force;
        this.params = params;
        this.environment = environment;
    }

    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public String getJob() {
        return job;
    }

    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public void setJob(String job) {
        this.job = job;
    }

    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public String getAt() {
        return at;
    }

    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public void setAt(String at) {
        this.at = at;
    }

    @JsonProperty("after")
    @JacksonXmlProperty(localName = "after", isAttribute = true)
    public String getAfter() {
        return after;
    }

    @JsonProperty("after")
    @JacksonXmlProperty(localName = "after", isAttribute = true)
    public void setAfter(String after) {
        this.after = after;
    }

    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public String getWebService() {
        return webService;
    }

    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public void setWebService(String webService) {
        this.webService = webService;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("force")
    @JacksonXmlProperty(localName = "force", isAttribute = true)
    public String getForce() {
        return force;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("force")
    @JacksonXmlProperty(localName = "force", isAttribute = true)
    public void setForce(String force) {
        this.force = force;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public Params getParams() {
        return params;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public EnviromentVariables getEnvironment() {
        return environment;
    }

    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public void setEnvironment(EnviromentVariables environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("job", job).append("at", at).append("after", after).append("webService", webService).append("force", force).append("params", params).append("environment", environment).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(environment).append(at).append(webService).append(force).append(after).append(job).append(params).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartJob) == false) {
            return false;
        }
        StartJob rhs = ((StartJob) other);
        return new EqualsBuilder().append(environment, rhs.environment).append(at, rhs.at).append(webService, rhs.webService).append(force, rhs.force).append(after, rhs.after).append(job, rhs.job).append(params, rhs.params).isEquals();
    }

}
