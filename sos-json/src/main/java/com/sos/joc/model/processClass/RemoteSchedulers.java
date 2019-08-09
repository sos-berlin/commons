
package com.sos.joc.model.processClass;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "remote_schedulers")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "select",
    "list"
})
public class RemoteSchedulers {

    /**
     * first, next
     * 
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    private String select;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("list")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    private List<RemoteScheduler> list = null;

    /**
     * first, next
     * 
     * @return
     *     The select
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    public String getSelect() {
        return select;
    }

    /**
     * first, next
     * 
     * @param select
     *     The select
     */
    @JsonProperty("select")
    @JacksonXmlProperty(localName = "select", isAttribute = true)
    public void setSelect(String select) {
        this.select = select;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The list
     */
    @JsonProperty("list")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    public List<RemoteScheduler> getList() {
        return list;
    }

    /**
     * 
     * (Required)
     * 
     * @param list
     *     The list
     */
    @JsonProperty("list")
    @JacksonXmlProperty(localName = "remote_scheduler", isAttribute = false)
    public void setList(List<RemoteScheduler> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(select).append(list).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RemoteSchedulers) == false) {
            return false;
        }
        RemoteSchedulers rhs = ((RemoteSchedulers) other);
        return new EqualsBuilder().append(select, rhs.select).append(list, rhs.list).isEquals();
    }

}
