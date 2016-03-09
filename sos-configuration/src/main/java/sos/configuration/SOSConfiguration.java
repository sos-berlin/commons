package sos.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import sos.connection.SOSConnection;
import sos.settings.SOSConnectionSettings;
import sos.settings.SOSProfileSettings;
import sos.settings.SOSSettings;
import sos.settings.SOSXMLSettings;
import sos.util.SOSArguments;
import sos.util.SOSLogger;
import sos.util.SOSString;

/** Klasse Configuration
 *
 * In dieser Klassen werden Parametern - entspricht ConfigurationItem Objekte -
 * gemergt.
 *
 * Der Herkunft der ConfigurationItem's sind: - Programmargumente - Scheduler
 * Parameter - Konfigurationsdateien - Datenbank
 *
 * Es existiert eine Meta Datei, in dem folgen Information beiinhaltet sind:
 *
 * 1. gültige Parametername 2. überprüft ob die Parameterwerte zu den
 * Parametername gültig sind 3. Abhängigkeiten der Parameternamen untereinander
 * 4. überprüfen der Abhängigkeiten mit Hilfe der boolean Operationen wie and
 * und or */
public class SOSConfiguration {

    private static final Logger LOGGER = Logger.getLogger(SOSConfiguration.class);
    private SOSConfigurationItem[] sosConfigurationItem = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromRequiredDefaults = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromSettings = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromScheduler = new SOSConfigurationItem[] {};
    private SOSConfigurationItem[] originParameterFromArguments = new SOSConfigurationItem[] {};
    private final SOSString sosString = new SOSString();
    private SOSLogger sosLogger = null;
    /** Programargumente k&ouml;nnen entweder als String[] oder als Properties
     * &uuml;bergeben werden */
    private String[] arguments = null;
    private Properties argumentsAsProperties = null;
    /** Scheduler Parameter */
    private Properties schedulerParams = null;
    private String settingsFile = null;
    private String settingsTablename = null;
    private String settingsApplicationname = null;
    private String settingsProfilename = null;
    /** SOSConnection Objekt wird verwendet in SOSConnectionSettings */
    private SOSConnection sosConnection = null;
    private String requiredDefaultFile = null;
    private SOSConfigurationRequiredItem ri = null;

    public SOSConfiguration(final String[] arguments_, final Properties schedulerParams_, final String settingsFile_, final String settingsProfilename_,
            final String requiredDefaultFile_, final SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;
            arguments = arguments_;
            schedulerParams = schedulerParams_;
            settingsFile = settingsFile_;
            settingsProfilename = settingsProfilename_;
            requiredDefaultFile = requiredDefaultFile_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }

    }

    public SOSConfiguration(final String[] arguments_, final Properties schedulerParams_, final String settingsFile_, final String settingsApplicationname_,
            final String settingsProfilename_, final String requiredDefaultFile_, final SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;
            arguments = arguments_;
            schedulerParams = schedulerParams_;
            settingsFile = settingsFile_;
            settingsApplicationname = settingsApplicationname_;
            settingsProfilename = settingsProfilename_;
            requiredDefaultFile = requiredDefaultFile_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }

    }

    public SOSConfiguration(final String[] arguments_, final String settingsFile_, final String settingsProfilename_, final SOSLogger sosLogger_)
            throws Exception {
        try {
            sosLogger = sosLogger_;
            arguments = arguments_;
            settingsFile = settingsFile_;
            settingsProfilename = settingsProfilename_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfiguration(final String[] arguments_, final SOSConnection sosConnection_, final String settingsTablename_,
            final String settingsApplicationname_, final String settingsProfilename_, final SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;
            arguments = arguments_;
            sosConnection = sosConnection_;
            settingsTablename = settingsTablename_;
            settingsApplicationname = settingsApplicationname_;
            settingsProfilename = settingsProfilename_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfiguration(final Properties arguments_, final String settingsFile_, final String settingsProfilename_, final SOSLogger sosLogger_)
            throws Exception {
        try {
            sosLogger = sosLogger_;
            argumentsAsProperties = arguments_;
            settingsFile = settingsFile_;
            settingsProfilename = settingsProfilename_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfiguration(final String settingsFile_, final String settingsProfilename_, final SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;
            settingsFile = settingsFile_;
            settingsProfilename = settingsProfilename_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfiguration(final String[] arguments_, final Properties schedulerParams_, final String settingsFile_, final String settingsProfilename_,
            final SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;
            arguments = arguments_;
            schedulerParams = schedulerParams_;
            settingsFile = settingsFile_;
            settingsProfilename = settingsProfilename_;
            init();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private void init() throws Exception {
        // Programm Arguments können vom Typ String[] oder Properties sein
        if (arguments != null) {
            readProgramArguments(arguments);
        } else {
            readProgramArguments(argumentsAsProperties);
        }
        // scheduler Parameter
        readSchedulerParams(schedulerParams);
        // SOSSettings bestimmen
        readSettings();
        // merged die Parameter
        mergeConfigurationItem();
    }

    private void readSettings() throws Exception {
        try {
            if (sosConnection != null && sosString.parseToString(settingsProfilename).length() > 0) {
                readConnectionSettings();
            } else if (sosString.parseToString(settingsFile).length() > 0 && sosString.parseToString(settingsProfilename).length() > 0) {
                readProfileSettings();
            } else {
                originParameterFromSettings = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Liest Einstellungen aus der Datenbank
     * 
     * @throws Exception */
    private void readConnectionSettings() throws Exception {
        try {
            if (sosConnection != null && sosString.parseToString(settingsProfilename).length() > 0) {
                SOSConnectionSettings settings = new SOSConnectionSettings(sosConnection, settingsTablename, settingsApplicationname, settingsProfilename, sosLogger);
                Properties p = settings.getSection();
                originParameterFromSettings = new SOSConfigurationItem[p.size()];
                Iterator it = p.keySet().iterator();
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
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    /** Liest Einstellungen aus der Konfigurationsdatei (*.ini oder *.xml)
     * 
     * @throws Exception */
    private void readProfileSettings() throws Exception {
        try {
            if (sosString.parseToString(settingsFile).length() > 0 && sosString.parseToString(settingsProfilename).length() > 0) {
                SOSSettings settings = null;
                Properties p = null;
                if (new java.io.File(settingsFile).getName().endsWith(".xml")) {
                    settings = new SOSXMLSettings(settingsFile, settingsProfilename, sosLogger);
                    p = settings.getSection(settingsApplicationname, settingsProfilename);
                } else {
                    settings = new SOSProfileSettings(settingsFile, settingsProfilename, sosLogger);
                    p = settings.getSection();
                }
                originParameterFromSettings = new SOSConfigurationItem[p.size()];
                Iterator it = p.keySet().iterator();
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
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private void readSchedulerParams(final Properties schedulerParams_) throws Exception {
        try {
            originParameterFromScheduler = new SOSConfigurationItem[] {};
            if (schedulerParams_ != null) {
                originParameterFromScheduler = new SOSConfigurationItem[schedulerParams_.size()];
                java.util.Iterator keys = schedulerParams_.keySet().iterator();
                int i = 0;
                while (keys.hasNext()) {
                    String key = sosString.parseToString(keys.next());
                    sosLogger.debug("..scheduler param = " + key);
                    originParameterFromScheduler[i] = new SOSConfigurationItem();
                    originParameterFromScheduler[i].setName(key);
                    originParameterFromScheduler[i].setValue(schedulerParams_.getProperty(key));
                    i++;
                }
            } else {
                originParameterFromScheduler = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private void readProgramArguments(final String[] arguments) throws Exception {
        try {
            if (arguments != null) {
                originParameterFromArguments = new SOSConfigurationItem[arguments.length];
                SOSArguments arguments_ = new SOSArguments(arguments, true);
                java.util.Iterator keys = arguments_.get_arguments().keySet().iterator();
                int i = 0;
                while (keys.hasNext()) {
                    String key = sosString.parseToString(keys.next());
                    String newKey = key.trim().startsWith("-") && key.trim().endsWith("=") & key.length() > 0 ? key.substring(1, key.length() - 1) : key;
                    originParameterFromArguments[i] = new SOSConfigurationItem();
                    originParameterFromArguments[i].setName(newKey);
                    originParameterFromArguments[i].setValue(arguments_.as_string(key.toString()));
                    i++;
                }
            } else {
                originParameterFromArguments = new SOSConfigurationItem[] {};
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    private void readProgramArguments(final Properties arguments) throws Exception {
        try {
            if (arguments != null) {
                originParameterFromArguments = new SOSConfigurationItem[arguments.size()];
                java.util.Iterator keys = arguments.keySet().iterator();
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
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
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
            if (strName != null && strName.length() > 0) {
                p.put(pstrPrefix + strName, item.getValue());
            }
        }
        return p;
    }

    /** @param parameter the parameter to set */
    public void setParameter(final SOSConfigurationItem[] parameter) {
        sosConfigurationItem = parameter;
    }

    /** p2 überschreibt die Werte von p1
     * 
     * @param p1
     * @param p2
     * @return */
    public SOSConfigurationItem[] merge(final SOSConfigurationItem[] p1, final SOSConfigurationItem[] p2) throws Exception {
        SOSConfigurationItem[] retVal = null;
        HashMap hp = new HashMap();
        for (SOSConfigurationItem element : p1) {
            String name = sosString.parseToString(element.getName());
            hp.put(name, element);
        }
        for (SOSConfigurationItem element : p2) {
            String name = sosString.parseToString(element.getName());
            hp.put(name, element);
        }
        Iterator it = hp.keySet().iterator();
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
                ri = new SOSConfigurationRequiredItem(sosLogger);
            } else {
                ri = new SOSConfigurationRequiredItem(requiredDefaultFile, sosLogger);
            }
            sosConfigurationItem = ri.check(sosConfigurationItem);
            return sosConfigurationItem;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public SOSConfigurationItem getConfigurationItemByName(final String name) throws Exception {
        try {
            SOSConfigurationItem item = null;
            if (ri != null && ri.getQuickConfigurationsItem() != null && ri.getQuickConfigurationsItem().containsKey(name)) {
                // das überprüfte ConfigurationItem wird hier übergeben
                item = (SOSConfigurationItem) ri.getQuickConfigurationsItem().get(name);
            }
            if (item == null) {
                item = new SOSConfigurationItem();
            }
            return item;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public HashMap getConfigurationsItems() {
        if (ri != null && ri.getQuickConfigurationsItem() != null) {
            // das überprüfte ConfigurationItem wird hier übergeben
            return ri.getQuickConfigurationsItem();
        } else {
            return new HashMap();
        }
    }

    public ArrayList getPasswordnames() {
        if (ri != null && ri.getPasswordNames() != null) {
            // alle Passwortnamen werden übergeben
            return ri.getPasswordNames();
        } else {
            return new ArrayList();
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
        sosConfigurationItem = configurationItem;
    }

    public void setLogger(final SOSLogger sosLogger) {
        this.sosLogger = sosLogger;
    }

    public void setArguments(final String[] arguments) throws Exception {
        this.arguments = arguments;
        try {
            readProgramArguments(arguments);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public void setArguments(final Properties arguments) throws Exception {
        argumentsAsProperties = arguments;
        try {
            readProgramArguments(argumentsAsProperties);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
        }
    }

    public void setSchedulerParams(final Properties schedulerParams_) {
        schedulerParams = schedulerParams_;
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

    /** test SOSProfileSettings
     * 
     * @param args
     * @param requiredDefaultFile
     * @param sosLogger */
    public static void test1(final String[] args, final String requiredDefaultFile, final sos.util.SOSLogger sosLogger) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSProfileSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            String settingsFile = "./testDateien/ftp_settings.ini";
            String profile = "remote_wilma";
            SOSConfiguration con = new SOSConfiguration(args, null, settingsFile, profile, sosLogger);
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

    /** test SOSXMLSettings
     * 
     * @param args
     * @param requiredDefaultFile
     * @param sosLogger */
    public static void test2(final String[] args, final String requiredDefaultFile, final sos.util.SOSLogger sosLogger) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSXMLSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            String settingsFile = "./testDateien/test_settings.xml";
            String settingsApplication = "defaults";
            String profile = "remote_wilma";
            SOSConfiguration con = new SOSConfiguration(args, null, settingsFile, settingsApplication, profile, requiredDefaultFile, sosLogger);
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

    /** test SOSConnectionSettings
     * 
     * @param args
     * @param iniSOSConnectionFile
     * @param requiredDefaultFile
     * @param sosLogger */
    public static void test3(final String[] args, final String iniSOSConnectionFile, final String requiredDefaultFile, final sos.util.SOSLogger sosLogger) {
        try {
            LOGGER.debug("~~~~~~~~~~~~~~~~~~ testen von SOSConnectionSettings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            SOSConnection sosConnection = null;
            try {
                LOGGER.debug("DB Connecting.. .");
                sosConnection = SOSConnection.createInstance(iniSOSConnectionFile, sosLogger);
                sosConnection.connect();
                LOGGER.debug("DB Connected");
            } catch (Exception e) {
                LOGGER.error(e);
            }
            String settingsTablename = "SETTINGS";
            String settingsApplication = "test_sosftp";
            String settingsProfilename = "test";
            SOSConfiguration con = new SOSConfiguration(args, sosConnection, settingsTablename, settingsApplication, settingsProfilename, sosLogger);
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
            // Folgende dateien werden zum Testen benötigt:
            // 1. ./testDateien/Configuration.xml
            // 2. ./testDateien/sos_settings.ini
            // 3. ./testDateien/ftp_settings.ini
            // 4. ./testDateien/test_settings.xml
            // 5. sos.util.configuration.TestJob.java --> liegt temporär da
            // 6. ./testDateien/TestParams.job.xml -> job für 5.
            // manuell erzeugte programmargumente soll zum Test dienen
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
            // 1- Testen von Programmargumente und SOSProfileSettings mit
            // Configuration.xml
            test1(args, requiredDefaultFile, sosLogger);
            // 2- Testen von Programmargumente und SOSXMLSettings mit
            // Configuration.xml
            test2(args, requiredDefaultFile, sosLogger);
            // 3- Testen von Programmargumente und SOSConnectionSettings mit
            // Configuration.xml
            test3(args, "./testDateien/sos_settings.ini", requiredDefaultFile, sosLogger);
        } catch (Exception e) {
            LOGGER.error("error : " + e.toString());
        }
    }

}
