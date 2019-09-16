
package com.sos.joc.model.joe.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
    @JacksonXmlProperty(localName = "force", isAttribute = true)
    private String force = "true";
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
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public String getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job", isAttribute = true)
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The at
     */
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public String getAt() {
        return at;
    }

    /**
     * 
     * @param at
     *     The at
     */
    @JsonProperty("at")
    @JacksonXmlProperty(localName = "at", isAttribute = true)
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * 
     * @return
     *     The after
     */
    @JsonProperty("after")
    @JacksonXmlProperty(localName = "after", isAttribute = true)
    public String getAfter() {
        return after;
    }

    /**
     * 
     * @param after
     *     The after
     */
    @JsonProperty("after")
    @JacksonXmlProperty(localName = "after", isAttribute = true)
    public void setAfter(String after) {
        this.after = after;
    }

    /**
     * 
     * @return
     *     The webService
     */
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public String getWebService() {
        return webService;
    }

    /**
     * 
     * @param webService
     *     The webService
     */
    @JsonProperty("webService")
    @JacksonXmlProperty(localName = "web_service", isAttribute = true)
    public void setWebService(String webService) {
        this.webService = webService;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The force
     */
    @JsonProperty("force")
    @JacksonXmlProperty(localName = "force", isAttribute = true)
    public String getForce() {
        return force;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param force
     *     The force
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
     * @return
     *     The params
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
     * @param params
     *     The params
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
     * @return
     *     The environment
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
     * @param environment
     *     The environment
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public void setEnvironment(EnviromentVariables environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(at).append(after).append(webService).append(force).append(params).append(environment).toHashCode();
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
        return new EqualsBuilder().append(job, rhs.job).append(at, rhs.at).append(after, rhs.after).append(webService, rhs.webService).append(force, rhs.force).append(params, rhs.params).append(environment, rhs.environment).isEquals();
    }

}
