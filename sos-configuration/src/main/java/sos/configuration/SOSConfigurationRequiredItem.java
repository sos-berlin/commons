package sos.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.util.SOSLogger;
import sos.util.SOSString;
import sos.xml.SOSXMLXPath;

public class SOSConfigurationRequiredItem {

    private static final Logger LOGGER = Logger.getLogger(SOSConfigurationRequiredItem.class);
    private SOSString sosString = new SOSString();
    private SOSXMLXPath xPath = null;
    private String msgUnknownParameter = "";
    private ArrayList checkIds = null;
    private HashMap quickConfigurationsItem = null;
    private SOSLogger sosLogger = null;
    private ArrayList passwordNames = null;
    private boolean allowOtherParamsNames = false;
    public static String REQUIRED_DEFAULT_PARAMETERS_FILENAME = "configuration.xml";

    public SOSConfigurationRequiredItem(SOSLogger sosLogger_) throws Exception {
        this.sosLogger = sosLogger_;
        init();
    }

    public SOSConfigurationRequiredItem(String filename, SOSLogger sosLogger_) throws Exception {
        this.sosLogger = sosLogger_;
        REQUIRED_DEFAULT_PARAMETERS_FILENAME = filename;
        init();
    }

    private void init() throws Exception {
        try {
            checkIds = new ArrayList();
            quickConfigurationsItem = new HashMap();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfigurationItem[] check(SOSConfigurationItem[] configurationItem) throws Exception {
        allowOtherParamsNames = true;
        try {
            allowOtherParamsNames = true;
            for (int i = 0; i < configurationItem.length; i++) {
                String name = configurationItem[i].getName();
                String value = configurationItem[i].getValue();
                if (configurationItem[i].isPassword()) {
                    getLogger().debug("check parameter [" + name + "=*****]");
                } else {
                    getLogger().debug("check parameter [" + name + "=" + value + "]");
                }
                String itemId = name + "_id";
                quickConfigurationsItem.put(itemId, configurationItem[i]);
                quickConfigurationsItem.put(name, configurationItem[i]);
            }
            if (msgUnknownParameter != null && !msgUnknownParameter.isEmpty()) {
                throw new Exception(msgUnknownParameter);
            }
            return configurationItem;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private SOSConfigurationItem[] mergeListOfMissingItemWithDefaults(ArrayList listOfMissingItemWithDefaults, SOSConfigurationItem[] configurationsItem)
            throws Exception {
        SOSConfigurationItem[] newconfigurationItem = configurationsItem;
        try {
            if (!listOfMissingItemWithDefaults.isEmpty()) {
                newconfigurationItem = new SOSConfigurationItem[configurationsItem.length + listOfMissingItemWithDefaults.size()];
                for (int j = 0; j < configurationsItem.length; j++) {
                    newconfigurationItem[j] = configurationsItem[j];
                }
                for (int i = 0; i < listOfMissingItemWithDefaults.size(); i++) {
                    newconfigurationItem[configurationsItem.length + i] = (SOSConfigurationItem) listOfMissingItemWithDefaults.get(i);
                }
                getLogger().debug("check again, cause new Defaultvalues change the Conditions");
            }
            return newconfigurationItem;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private SOSConfigurationItem getNewConfigurationItem(String itemId) throws Exception {
        SOSConfigurationItem item = null;
        try {
            Node n = xPath.selectSingleNode("//Configurations/items/item[@itemId='" + itemId + "'] ");
            if (n == null) {
                if (!msgUnknownParameter.isEmpty()) {
                    msgUnknownParameter = msgUnknownParameter + "\n";
                }
                msgUnknownParameter = msgUnknownParameter + " invalid parameter itemId: " + itemId + ".";
                return null;
            } else {
                item = new SOSConfigurationItem();
                item.setItemId(itemId);
                org.w3c.dom.NamedNodeMap map = n.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    if ("default_value".equalsIgnoreCase(map.item(j).getNodeName())) {
                        item.setDefaults(map.item(j).getNodeValue());
                        item.setValue(map.item(j).getNodeValue());
                    } else if ("checkId".equalsIgnoreCase(map.item(j).getNodeName())) {
                        String checkName = map.item(j).getNodeValue();
                        if (!checkIds.contains(checkName)) {
                            checkIds.add(checkName);
                        }
                    } else if ("name".equalsIgnoreCase(map.item(j).getNodeName())) {
                        item.setName(map.item(j).getNodeValue());
                    }
                }
            }
            return item;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }

    }

    public SOSLogger getLogger() {
        return sosLogger;
    }

    public void setLogger(SOSLogger sosLogger) {
        this.sosLogger = sosLogger;
    }

    public HashMap getQuickConfigurationsItem() {
        return quickConfigurationsItem;
    }

    public static void main(String[] args) {
        try {
            sos.util.SOSLogger sosLogger = new sos.util.SOSStandardLogger(9);
            SOSConfigurationItem pOperation = new SOSConfigurationItem();
            pOperation.setName("operation");
            pOperation.setValue("send");
            SOSConfigurationItem pProtocol = new SOSConfigurationItem();
            pProtocol.setName("protocol");
            pProtocol.setValue("ftp");
            SOSConfigurationItem pHost = new SOSConfigurationItem();
            pHost.setName("host");
            pHost.setValue("localhost");
            SOSConfigurationItem pUser = new SOSConfigurationItem();
            pUser.setName("user");
            pUser.setValue("sos");
            SOSConfigurationItem pfilePath = new SOSConfigurationItem();
            pfilePath.setName("file_path");
            pfilePath.setValue("c:/temp/1.xml");
            SOSConfigurationItem sshProxyHost = new SOSConfigurationItem();
            sshProxyHost.setName("ssh_proxy_host");
            sshProxyHost.setValue("ssh_proxy_host.SOS");
            SOSConfigurationItem sshProxyPort = new SOSConfigurationItem();
            sshProxyPort.setName("ssh_proxy_port");
            sshProxyPort.setValue("1000");
            SOSConfigurationItem sshProxyUser = new SOSConfigurationItem();
            sshProxyUser.setName("ssh_proxy_user");
            sshProxyUser.setValue("sos");
            SOSConfigurationItem[] p1 = new SOSConfigurationItem[] { pOperation, pfilePath, pProtocol, pUser, sshProxyHost, sshProxyPort, sshProxyUser };
            sosLogger.debug("***************************Start configuration Item vorher************************");
            for (int i = 0; i < p1.length; i++) {
                sosLogger.debug(i + "'te Parameter");
                sosLogger.debug("name    =\t " + p1[i].getName());
                sosLogger.debug("value   =\t " + p1[i].getValue());
                sosLogger.debug("default =\t " + p1[i].getDefaults());
                sosLogger.debug("itemId  =\t " + p1[i].getItemId());
                sosLogger.debug("");
            }
            sosLogger.debug("**********************************************************************************");
            SOSConfigurationRequiredItem ri = new SOSConfigurationRequiredItem("J:/E/java/mo/doc/sosftp/Redesign/Configuration.xml", sosLogger);
            p1 = ri.check(p1);
            sosLogger.debug("***************************Start configuration Item nachher************************");
            for (int i = 0; i < p1.length; i++) {
                sosLogger.debug(i + "'te Parameter");
                sosLogger.debug("name    =\t " + p1[i].getName());
                sosLogger.debug("value   =\t " + p1[i].getValue());
                sosLogger.debug("default =\t " + p1[i].getDefaults());
                sosLogger.debug("itemId  =\t " + p1[i].getItemId());
                sosLogger.debug("");
            }
            sosLogger.debug("**********************************************************************************");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public ArrayList getPasswordNames() {
        return passwordNames;
    }

}
