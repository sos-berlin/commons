
package com.sos.joc.model.joe.other;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.JSObjectEdit;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * edit folders
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "folder_edit")
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({

})
public class FolderEdit
    extends JSObjectEdit
{


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
