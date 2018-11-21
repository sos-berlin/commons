
package com.sos.joc.model.docu;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * documentation
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "path",
    "name",
    "type",
    "title"
})
public class Documentation {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    /**
     * Mime Type of the Docu
     * <p>
     * The documentation can be a HTML, XML or MarkDown(MD).
     * 
     */
    @JsonProperty("type")
    private MimeType type;
    @JsonProperty("title")
    private String title;

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mime Type of the Docu
     * <p>
     * The documentation can be a HTML, XML or MarkDown(MD).
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public MimeType getType() {
        return type;
    }

    /**
     * Mime Type of the Docu
     * <p>
     * The documentation can be a HTML, XML or MarkDown(MD).
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(MimeType type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(path).append(name).append(type).append(title).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Documentation) == false) {
            return false;
        }
        Documentation rhs = ((Documentation) other);
        return new EqualsBuilder().append(id, rhs.id).append(path, rhs.path).append(name, rhs.name).append(type, rhs.type).append(title, rhs.title).isEquals();
    }

}
