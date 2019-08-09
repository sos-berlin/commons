package sos.settings;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

public abstract class SOSSettings {

    protected String source = "";
    protected String section = "";
    protected SOSLogger logger;
    protected String entry = "";
    protected String author = "SOS";
    protected Hashtable sources = new Hashtable();
    protected String entryApplication = "APPLICATION";
    protected String entrySection = "SECTION";
    protected String entryName = "NAME";
    protected String entryValue = "VALUE";
    protected String entryTitle = "TITLE";
    protected boolean ignoreCase = false;

    public SOSSettings(String source) throws Exception {
        if (SOSString.isEmpty(source)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid source name !!.");
        }
        this.source = source;
    }

    public SOSSettings(String source, SOSLogger logger) throws Exception {
        if (SOSString.isEmpty(source)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid source name !!.");
        }
        if (logger == null) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid logger object !!.");
        }
        this.logger = logger;
        this.source = source;
    }

    public SOSSettings(String source, String section) throws Exception {
        if (SOSString.isEmpty(source)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid source name !!.");
        }
        if (SOSString.isEmpty(section)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid section name !!.");
        }
        this.source = source;
        this.section = section;
    }

    public SOSSettings(String source, String section, SOSLogger logger) throws Exception {
        if (SOSString.isEmpty(source)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid source name !!.");
        }
        if (SOSString.isEmpty(section)) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid section name !!.");
        }
        if (logger == null) {
            throw new Exception(SOSClassUtil.getMethodName() + " sosLogger object must not be null.");
        }
        this.source = source;
        this.section = section;
        this.logger = logger;
    }

    public abstract Properties getSection() throws Exception;

    public abstract Properties getSection(String section) throws Exception;

    public abstract Properties getSection(String application, String section) throws Exception;

    public abstract List<String> getSections() throws Exception;

    public abstract String getSectionEntry(String entry) throws Exception;

    public abstract void setKeysToLowerCase() throws Exception;

    public abstract void setKeysToUpperCase() throws Exception;

    public abstract void setIgnoreCase(boolean ignoreCase);

    public abstract boolean getIgnoreCase();

}
