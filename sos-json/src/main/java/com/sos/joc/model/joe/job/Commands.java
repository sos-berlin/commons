
package com.sos.joc.model.joe.job;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.order.AddOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "commands")
@JsonPropertyOrder({
    "onExitCode",
    "addOrders",
    "startJobs"
})
public class Commands {

    /**
     * possible values: success, error or space separated collection of numbers or a unix signal
     * 
     */
    @JsonProperty("onExitCode")
    @JsonPropertyDescription("possible values: success, error or space separated collection of numbers or a unix signal")
    @JacksonXmlProperty(localName = "on_exit_code", isAttribute = true)
    private String onExitCode;
    @JsonProperty("addOrders")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    private List<AddOrder> addOrders = null;
    @JsonProperty("startJobs")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    private List<StartJob> startJobs = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Commands() {
    }

    /**
     * 
     * @param addOrders
     * @param onExitCode
     * @param startJobs
     */
    public Commands(String onExitCode, List<AddOrder> addOrders, List<StartJob> startJobs) {
        super();
        this.onExitCode = onExitCode;
        this.addOrders = addOrders;
        this.startJobs = startJobs;
    }

    /**
     * possible values: success, error or space separated collection of numbers or a unix signal
     * 
     */
    @JsonProperty("onExitCode")
    @JacksonXmlProperty(localName = "on_exit_code", isAttribute = true)
    public String getOnExitCode() {
        return onExitCode;
    }

    /**
     * possible values: success, error or space separated collection of numbers or a unix signal
     * 
     */
    @JsonProperty("onExitCode")
    @JacksonXmlProperty(localName = "on_exit_code", isAttribute = true)
    public void setOnExitCode(String onExitCode) {
        this.onExitCode = onExitCode;
    }

    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public List<AddOrder> getAddOrders() {
        return addOrders;
    }

    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public void setAddOrders(List<AddOrder> addOrders) {
        this.addOrders = addOrders;
    }

    @JsonProperty("startJobs")
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    public List<StartJob> getStartJobs() {
        return startJobs;
    }

    @JsonProperty("startJobs")
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    public void setStartJobs(List<StartJob> startJobs) {
        this.startJobs = startJobs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("onExitCode", onExitCode).append("addOrders", addOrders).append("startJobs", startJobs).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(addOrders).append(startJobs).append(onExitCode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Commands) == false) {
            return false;
        }
        Commands rhs = ((Commands) other);
        return new EqualsBuilder().append(addOrders, rhs.addOrders).append(startJobs, rhs.startJobs).append(onExitCode, rhs.onExitCode).isEquals();
    }

}
