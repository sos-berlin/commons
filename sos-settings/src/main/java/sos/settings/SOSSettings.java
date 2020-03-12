package sos.settings;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;
import sos.util.SOSString;

public abstract class SOSSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSSettings.class);
    protected String source = "";
    protected String section = "";
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
