
package com.sos.joc.model.joe.job;

import java.util.List;
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
@JsonPropertyOrder({
    "addOrders",
    "startJobs"
})
public class Commands {

    @JsonProperty("addOrders")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "add_order", isAttribute = false)
    private List<AddOrder> addOrders = null;
    @JsonProperty("startJobs")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "start_job", isAttribute = false)
    private List<StartJob> startJobs = null;

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
        return new ToStringBuilder(this).append("addOrders", addOrders).append("startJobs", startJobs).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(addOrders).append(startJobs).toHashCode();
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
        return new EqualsBuilder().append(addOrders, rhs.addOrders).append(startJobs, rhs.startJobs).isEquals();
    }

}
