//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse for anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="configuration.file" type="{}configuration.file"/>
 *           &lt;element name="configuration.directory" type="{}configuration.directory"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "configurationFileOrConfigurationDirectory"
})
@XmlRootElement(name = "supervisor.remote_scheduler.configuration.fetch_updated_files")
public class SupervisorRemoteSchedulerConfigurationFetchUpdatedFiles {

    @XmlElements({
        @XmlElement(name = "configuration.file", type = ConfigurationFile.class),
        @XmlElement(name = "configuration.directory", type = ConfigurationDirectory.class)
    })
    protected List<Object> configurationFileOrConfigurationDirectory;

    /**
     * Gets the value of the configurationFileOrConfigurationDirectory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configurationFileOrConfigurationDirectory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigurationFileOrConfigurationDirectory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationFile }
     * {@link ConfigurationDirectory }
     * 
     * 
     */
    public List<Object> getConfigurationFileOrConfigurationDirectory() {
        if (configurationFileOrConfigurationDirectory == null) {
            configurationFileOrConfigurationDirectory = new ArrayList<Object>();
        }
        return this.configurationFileOrConfigurationDirectory;
    }

}
