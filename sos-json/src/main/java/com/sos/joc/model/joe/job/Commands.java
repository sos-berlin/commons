
package com.sos.joc.model.joe.job;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Generated("org.jsonschema2pojo")
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
        this.onExitCode = onExitCode;
        this.addOrders = addOrders;
        this.startJobs = startJobs;
    }

    /**
     * possible values: success, error or space separated collection of numbers or a unix signal
     * 
     * @return
     *     The onExitCode
     */
    @JsonProperty("onExitCode")
    @JacksonXmlProperty(localName = "on_exit_code", isAttribute = true)
    public String getOnExitCode() {
        return onExitCode;
    }

    /**
     * possible values: success, error or space separated collection of numbers or a unix signal
     * 
     * @param onExitCode
     *     The onExitCode
     */
    @JsonProperty("onExitCode")
    @JacksonXmlProperty(localName = "on_exit_code", isAttribute = true)
    public void setOnExitCode(String onExitCode) {
        this.onExitCode = onExitCode;
    }

    /**
     * 
     * @return
     *     The addOrders
     */
    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public List<AddOrder> getAddOrders() {
        return addOrders;
    }

    /**
     * 
     * @param addOrders
     *     The addOrders
     */
    @JsonProperty("addOrders")
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    public void setAddOrders(List<AddOrder> addOrders) {
        this.addOrders = addOrders;
    }

    /**
     * 
     * @return
     *     The startJobs
     */
    @JsonProperty("startJobs")
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    public List<StartJob> getStartJobs() {
        return startJobs;
    }

    /**
     * 
     * @param startJobs
     *     The startJobs
     */
    @JsonProperty("startJobs")
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    public void setStartJobs(List<StartJob> startJobs) {
        this.startJobs = startJobs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(onExitCode).append(addOrders).append(startJobs).toHashCode();
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
        return new EqualsBuilder().append(onExitCode, rhs.onExitCode).append(addOrders, rhs.addOrders).append(startJobs, rhs.startJobs).isEquals();
    }

}
