package sos.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import sos.connection.SOSConnection;
import sos.settings.SOSConnectionSettings;
import sos.settings.SOSProfileSettings;
import sos.settings.SOSSettings;
import sos.settings.SOSXMLSettings;
import sos.util.SOSArguments;
import sos.util.SOSString;

public class SOSConfiguration {

    private static final Logger LOGGER = Logger.getLogger(SOSConfiguration.class);
    private SOSConfigurationItem[] sosConfigurationItem = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromRequiredDefaults = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromSettings = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromScheduler = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromArguments = new SOSConfigurationItem[] {};
    private final SOSString sosString = new SOSString();
    private String[] arguments = null;
    private Properties argumentsAsProperties = null;
    private Properties schedulerParams = null;
    private String settingsFile = null;
    private String settingsTablename = null;
    private String settingsApplicationname = null;
    private String settingsProfilename = null;
    private SOSConnection sosConnection = null;
    private String requiredDefaultFile = null;
    private SOSConfigurationRequiredItem ri = null;

    public SOSConfiguration(final String[] arguments, final Properties schedulerParams, final String settingsFile,
            final String settingsProfilename, final String requiredDefaultFile) throws Exception {
        try {
            this.arguments = arguments;
            this.schedulerParams = schedulerParams;
            this.settingsFile = settingsFile;
            this.settingsProfilename = settingsProfilename;
            this.requiredDefaultFile = requiredDefaultFile;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final String[] arguments, final Properties schedulerParams, final String settingsFile,
            final String settingsApplicationname, final String settingsProfilename, final String requiredDefaultFile) throws Exception {
        try {
            this.arguments = arguments;
            this.schedulerParams = schedulerParams;
            this.settingsFile = settingsFile;
            this.settingsApplicationname = settingsApplicationname;
            this.settingsProfilename = settingsProfilename;
            this.requiredDefaultFile = requiredDefaultFile;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final String[] arguments, final String settingsFile, final String settingsProfilename) throws Exception {
        try {
            this.arguments = arguments;
            this.settingsFile = settingsFile;
            this.settingsProfilename = settingsProfilename;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final String[] arguments, final SOSConnection sosConnection, final String settingsTablename,
            final String settingsApplicationname, final String settingsProfilename) throws Exception {
        try {
            this.arguments = arguments;
            this.sosConnection = sosConnection;
            this.settingsTablename = settingsTablename;
            this.settingsApplicationname = settingsApplicationname;
            this.settingsProfilename = settingsProfilename;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final Properties arguments, final String settingsFile, final String settingsProfilename) throws Exception {
        try {
            this.argumentsAsProperties = arguments;
            this.settingsFile = settingsFile;
            this.settingsProfilename = settingsProfilename;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final String settingsFile, final String settingsProfilename) throws Exception {
        try {
            this.settingsFile = settingsFile;
            this.settingsProfilename = settingsProfilename;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfiguration(final String[] arguments, final Properties schedulerParams, final String settingsFile,
            final String settingsProfilename) throws Exception {
        try {
            this.arguments = arguments;
            this.schedulerParams = schedulerParams;
            this.settingsFile = settingsFile;
            this.settingsProfilename = settingsProfilename;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    private void init() throws Exception {
        if (arguments != null) {
            readProgramArguments(arguments);
        } else {
            readProgramArguments(argumentsAsProperties);
        }
        readSchedulerParams(schedulerParams);
        readSettings();
        mergeConfigurationItem();
    }

    private void readSettings() throws Exception {
        try {
            if (sosConnection != null && !sosString.parseToString(settingsProfilename).isEmpty()) {
                readConnectionSettings();
            } else if (!sosString.parseToString(settingsFile).isEmpty() && !sosString.parseToString(settingsProfilename).isEmpty()) {
                readProfileSettings();
            } else {
                originParameterFromSettings = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readConnectionSettings() throws Exception {
        try {
            if (sosConnection != null && !sosString.parseToString(settingsProfilename).isEmpty()) {
                SOSConnectionSettings settings =
                        new SOSConnectionSettings(sosConnection, settingsTablename, settingsApplicationname, settingsProfilename);
                Properties p = settings.getSection();
                originParameterFromSettings = new SOSConfigurationItem[p.size()];
                Iterator<Object> it = p.keySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    String key = sosString.parseToString(it.next());
                    originParameterFromSettings[i] = new SOSConfigurationItem();
                    originParameterFromSettings[i].setName(key);
                    originParameterFromSettings[i].setValue(p.getProperty(key));
                    i++;
                }
            } else {
                originParameterFromSettings = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    private void readProfileSettings() throws Exception {
        try {
            if (!sosString.parseToString(settingsFile).isEmpty() && !sosString.parseToString(settingsProfilename).isEmpty()) {
                SOSSettings settings = null;
                Properties p = null;
                if (new java.io.File(settingsFile).getName().endsWith(".xml")) {
                    settings = new SOSXMLSettings(settingsFile, settingsProfilename);
                    p = settings.getSection(settingsApplicationname, settingsProfilename);
                } else {
                    settings = new SOSProfileSettings(settingsFile, settingsProfilename);
                    p = settings.getSection();
                }
                originParameterFromSettings = new SOSConfigurationItem[p.size()];
                Iterator<Object> it = p.keySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    String key = sosString.parseToString(it.next());
                    originParameterFromSettings[i] = new SOSConfigurationItem();
                    originParameterFromSettings[i].setName(key);
                    originParameterFromSettings[i].setValue(p.getProperty(key));
                    i++;
                }
            } else {
                originParameterFromSettings = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    private void readSchedulerParams(final Properties schedulerParams) throws Exception {
        try {
            originParameterFromScheduler = new SOSConfigurationItem[] {};
            if (schedulerParams != null) {
                originParameterFromScheduler = new SOSConfigurationItem[schedulerParams.size()];
                Iterator<Object> keys = schedulerParams.keySet().iterator();
                int i = 0;
                while (keys.hasNext()) {
                    String key = sosString.parseToString(keys.next());
                    LOGGER.debug("..scheduler param = " + key);
                    originParameterFromScheduler[i] = new SOSConfigurationItem();
                    originParameterFromScheduler[i].setName(key);
                    originParameterFromScheduler[i].setValue(schedulerParams.getProperty(key));
                    i++;
                }
            } else {
                originParameterFromScheduler = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    private void readProgramArguments(final String[] arguments) throws Exception {
        try {
            if (arguments != null) {
                originParameterFromArguments = new SOSConfigurationItem[arguments.length];
                SOSArguments arguments_ = new SOSArguments(arguments, true);
                Iterator<String> keys = arguments_.getArguments().keySet().iterator();
                int i = 0;
                while (keys.hasNext()) {
                    String key = sosString.parseToString(keys.next());
                    String newKey = key.trim().startsWith("-") && key.trim().endsWith("=") & !key.isEmpty() ? key.substring(1, key.length() - 1) : key;
                    originParameterFromArguments[i] = new SOSConfigurationItem();
                    originParameterFromArguments[i].setName(newKey);
                    originParameterFromArguments[i].setValue(arguments_.asString(key.toString()));
                    i++;
                }
            } else {
                originParameterFromArguments = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    private void readProgramArguments(final Properties arguments) throws Exception {
        try {
            if (arguments != null) {
                originParameterFromArguments = new SOSConfigurationItem[arguments.size()];
                Iterator<Object> keys = arguments.keySet().iterator();
                int i = 0;
                while (keys.hasNext()) {
                    String key = sosString.parseToString(keys.next());
                    originParameterFromArguments[i] = new SOSConfigurationItem();
                    originParameterFromArguments[i].setName(key);
                    originParameterFromArguments[i].setValue(sosString.parseToString(arguments.get(key)));
                    i++;
                }
            } else {
                originParameterFromArguments = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfigurationItem[] mergeConfigurationItem() throws Exception {
        sosConfigurationItem = merge(new SOSConfigurationItem[0], originParameterFromSettings);
        sosConfigurationItem = merge(sosConfigurationItem, originParameterFromScheduler);
        sosConfigurationItem = merge(sosConfigurationItem, originParameterFromArguments);
        return sosConfigurationItem;
    }

    public SOSConfigurationItem[] getParameter() throws Exception {
        return sosConfigurationItem;
    }

    public Properties getParameterAsProperties() throws Exception {
        return getParameterAsProperties("");
    }

    public Properties getParameterAsProperties(final String pstrPrefix) throws Exception {
        Properties p = new Properties();
        for (SOSConfigurationItem item : sosConfigurationItem) {
            String strName = item.getName();
            if (strName != null && !strName.isEmpty()) {
                p.put(pstrPrefix + strName, item.getValue());
            }
        }
        return p;
    }

    public void setParameter(final SOSConfigurationItem[] parameter) {
        sosConfigurationItem = parameter;
    }

    public SOSConfigurationItem[] merge(final SOSConfigurationItem[] p1, final SOSConfigurationItem[] p2) throws Exception {
        SOSConfigurationItem[] retVal = null;
        Map<String, SOSConfigurationItem> hp = new HashMap<String, SOSConfigurationItem>();
        for (SOSConfigurationItem element : p1) {
            String name = sosString.parseToString(element.getName());
            hp.put(name, element);
        }
        for (SOSConfigurationItem element : p2) {
            String name = sosString.parseToString(element.getName());
            hp.put(name, element);
        }
        Iterator<String> it = hp.keySet().iterator();
        retVal = new SOSConfigurationItem[hp.keySet().size()];
        for (int i = 0; it.hasNext(); i++) {
            Object key = it.next();
            retVal[i] = (SOSConfigurationItem) hp.get(key);
        }
        return retVal;
    }

    public SOSConfigurationItem[] checkConfigurationItems() throws Exception {
        return checkConfigurationItems(requiredDefaultFile);
    }

    public SOSConfigurationItem[] checkConfigurationItems(final String requiredDefaultFile) throws Exception {
        try {
            if (requiredDefaultFile == null) {
                ri = new SOSConfigurationRequiredItem();
            } else {
                ri = new SOSConfigurationRequiredItem(requiredDefaultFile);
            }
            sosConfigurationItem = ri.check(sosConfigurationItem);
            return sosConfigurationItem;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public SOSConfigurationItem getConfigurationItemByName(final String name) throws Exception {
        try {
            SOSConfigurationItem item = null;
            if (ri != null && ri.getQuickConfigurationsItem() != null && ri.getQuickConfigurationsItem().containsKey(name)) {
                item = (SOSConfigurationItem) ri.getQuickConfigurationsItem().get(name);
            }
            if (item == null) {
                item = new SOSConfigurationItem();
            }
            return item;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public Map<String, SOSConfigurationItem> getConfigurationsItems() {
        if (ri != null && ri.getQuickConfigurationsItem() != null) {
            return ri.getQuickConfigurationsItem();
        } else {
            return new HashMap<String, SOSConfigurationItem>();
        }
    }

    public List<String> getPasswordnames() {
        if (ri != null && ri.getPasswordNames() != null) {
            return ri.getPasswordNames();
        } else {
            return new ArrayList<String>();
        }
    }

    public SOSConfigurationItem[] getOriginParameterFromRequiredDefaults() {
        return originParameterFromRequiredDefaults;
    }

    public void setOriginParameterFromRequiredDefaults(final SOSConfigurationItem[] originParameterFromRequiredDefaults) {
        this.originParameterFromRequiredDefaults = originParameterFromRequiredDefaults;
    }

    public SOSConfigurationItem[] getOriginParameterFromSettings() {
        return originParameterFromSettings;
    }

    public void setOriginParameterFromSettings(final SOSConfigurationItem[] originParameterFromSettings) {
        this.originParameterFromSettings = originParameterFromSettings;
    }

    public SOSConfigurationItem[] getOriginParameterFromScheduler() {
        return originParameterFromScheduler;
    }

    public void setOriginParameterFromScheduler(final SOSConfigurationItem[] originParameterFromScheduler) {
        this.originParameterFromScheduler = originParameterFromScheduler;
    }

    public SOSConfigurationItem[] getOriginParameterFromArguments() {
        return originParameterFromArguments;
    }

    public void setOriginParameterFromArguments(final SOSConfigurationItem[] originParameterFromArguments) {
        this.originParameterFromArguments = originParameterFromArguments;
    }

    public void setConfigurationItem(final SOSConfigurationItem[] configurationItem) {
        this.sosConfigurationItem = configurationItem;
    }

    public void setArguments(final String[] arguments) throws Exception {
        this.arguments = arguments;
        try {
            readProgramArguments(arguments);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public void setArguments(final Properties arguments) throws Exception {
        argumentsAsProperties = arguments;
        try {
            readProgramArguments(argumentsAsProperties);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.getMessage(), e);
        }
    }

    public void setSchedulerParams(final Properties schedulerParams) {
        this.schedulerParams = schedulerParams;
    }

    public void setSettingsFile(final String settingsFile) {
        this.settingsFile = settingsFile;
    }

    public void setSettingsProfilename(final String settingsProfilename) {
        this.settingsProfilename = settingsProfilename;
    }

    public void setRequiredDefaultFile(final String requiredDefaultFile) {
        this.requiredDefaultFile = requiredDefaultFile;
    }

    public static void test1(final String[] args, final String requiredDefaultFile) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSProfileSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            String settingsFile = "./testDateien/ftp_settings.ini";
            String profile = "remote_wilma";
            SOSConfiguration con = new SOSConfiguration(args, null, settingsFile, profile);
            SOSConfigurationItem[] p2 = con.getParameter();
            LOGGER.debug("*************************** Hier sind die Parameter ohne Defaults und Überprüfung ***************************");
            for (int i = 0; i < p2.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p2[i].getName() + "\n\tvalue=" + p2[i].getValue() + "\n\tdefault=" + p2[i].getDefaults()
                        + "\n\titemId=" + p2[i].getItemId() + "\n\tpassword=" + p2[i].isPassword());
            }

            LOGGER.debug("*************************** Start configuration Item nachdem Überprüfung und ggf. mit Defaults ************************");
            SOSConfigurationItem[] p1 = con.checkConfigurationItems(requiredDefaultFile);
            for (int i = 0; i < p1.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p1[i].getName() + "\n\tvalue=" + p1[i].getValue() + "\n\tdefault=" + p1[i].getDefaults()
                        + "\n\titemId=" + p1[i].getItemId() + "\n\tpassword=" + p1[i].isPassword());
            }
            LOGGER.debug("**********************************************************************************");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void test2(final String[] args, final String requiredDefaultFile) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSXMLSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            String settingsFile = "./testDateien/test_settings.xml";
            String settingsApplication = "defaults";
            String profile = "remote_wilma";
            SOSConfiguration con = new SOSConfiguration(args, null, settingsFile, settingsApplication, profile, requiredDefaultFile);
            SOSConfigurationItem[] p2 = con.getParameter();
            LOGGER.debug("*************************** Hier sind die Parameter ohne Defaults und Überprüfung ***************************");
            for (int i = 0; i < p2.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p2[i].getName() + "\n\tvalue=" + p2[i].getValue() + "\n\tdefault=" + p2[i].getDefaults()
                        + "\n\titemId=" + p2[i].getItemId() + "\n\tpassword=" + p2[i].isPassword());
            }
            LOGGER.debug("*************************** Start configuration Item nachdem Überprüfung und ggf. mit Defaults ************************");
            SOSConfigurationItem[] p1 = con.checkConfigurationItems(requiredDefaultFile);
            for (int i = 0; i < p1.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p1[i].getName() + "\n\tvalue=" + p1[i].getValue() + "\n\tdefault=" + p1[i].getDefaults()
                        + "\n\titemId=" + p1[i].getItemId() + "\n\tpassword=" + p1[i].isPassword());
            }
            LOGGER.debug("**********************************************************************************");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void test3(final String[] args, final String iniSOSConnectionFile, final String requiredDefaultFile) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSConnectionSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            SOSConnection sosConnection = null;
            try {
                LOGGER.debug("DB Connecting.. .");
                sosConnection = SOSConnection.createInstance(iniSOSConnectionFile);
                sosConnection.connect();
                LOGGER.debug("DB Connected");
            } catch (Exception e) {
                LOGGER.error(e);
            }
            String settingsTablename = "SETTINGS";
            String settingsApplication = "test_sosftp";
            String settingsProfilename = "test";
            SOSConfiguration con = new SOSConfiguration(args, sosConnection, settingsTablename, settingsApplication, settingsProfilename);
            SOSConfigurationItem[] p2 = con.getParameter();
            LOGGER.debug("*************************** Hier sind die Parameter ohne Defaults und Überprüfung ***************************");
            for (int i = 0; i < p2.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p2[i].getName() + "\n\tvalue=" + p2[i].getValue() + "\n\tdefault=" + p2[i].getDefaults()
                        + "\n\titemId=" + p2[i].getItemId() + "\n\tpassword=" + p2[i].isPassword());
            }
            SOSConfigurationItem[] p1 = con.checkConfigurationItems(requiredDefaultFile);
            LOGGER.debug("*************************** Start configuration Item nachdem Überprüfung und ggf. mit Defaults ************************");
            for (int i = 0; i < p1.length; i++) {
                LOGGER.debug(i + "'te Parameter \n\tname=" + p1[i].getName() + "\n\tvalue=" + p1[i].getValue() + "\n\tdefault=" + p1[i].getDefaults()
                        + "\n\titemId=" + p1[i].getItemId() + "\n\tpassword=" + p1[i].isPassword());
            }
            LOGGER.debug("**********************************************************************************");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            if (args == null || args.length == 0) {
                args = new String[] { "-operation=receive", "-remove_files=true", "-user=1234" };
            }
            if (args != null && args.length > 0) {
                LOGGER.debug("~~~~~~~~~ Programm argumente sind: ");
                for (String arg : args) {
                    LOGGER.debug(arg + ";");
                }
                LOGGER.debug("~~~~~~~~~~~~~~~");
            }
            String requiredDefaultFile = "./testDateien/Configuration.xml";
            sos.util.SOSLogger sosLogger = new sos.util.SOSStandardLogger(10);
            test1(args, requiredDefaultFile);
            test2(args, requiredDefaultFile);
            test3(args, "./testDateien/sos_settings.ini", requiredDefaultFile);
        } catch (Exception e) {
            LOGGER.error("error : " + e.getMessage(), e);
        }
    }

}