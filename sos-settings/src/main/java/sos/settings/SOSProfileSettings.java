package sos.settings;

/** Andreas Püschel */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;
import sos.util.SOSString;

public class SOSProfileSettings extends sos.settings.SOSSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSProfileSettings.class);
    private static final Pattern SECTION_PATTERN = Pattern.compile("^\\s*\\[([^\\]]*)\\].*$");
    private static final Pattern ENTRY_PATTERN = Pattern.compile("^([^=#;]+)[ \t\n]*=(.*)$");
    private final ArrayList<String> sections = new ArrayList<String>();
    private final Properties entries = new Properties();
    protected boolean lowerCase = true;

    public SOSProfileSettings(final String source) throws Exception {
        super(source);
        this.load();
    }

    public SOSProfileSettings(final String source, final String section) throws Exception {
        super(source, section);
        this.load();
    }

    private void load() throws Exception {
        String sectionName = null;
        BufferedReader in = null;
        String key = null;
        String value = null;
        String line = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                throw new Exception("couldn't find settings-file [" + file.getAbsolutePath() + "].");
            }
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                line = line.replaceAll("\\r", "");
                Matcher matcher = SECTION_PATTERN.matcher(line);
                if (matcher.matches()) {
                    sectionName = matcher.group(1);
                    sections.add(sectionName);
                } else {
                    matcher = ENTRY_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        key = matcher.group(1).trim();
                        value = matcher.group(2).trim();
                        entries.put(sectionName + "#" + key, value);
                    }
                }
            }

            if (LOGGER.isDebugEnabled()) {
                // SOSClassUtil.printStackTrace(true);
                LOGGER.debug(SOSClassUtil.getMethodName() + ": profile [" + source + "] successfully loaded.");
            }
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // no exception handling
                }
            }
        }
    }

    public String getSectionEntry(final String section, final String entry) throws Exception {
        String value;
        try {
            value = entries.getProperty(section + "#" + entry);
            return value;
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public String getSectionEntry(final String entry) throws Exception {
        try {
            return getSectionEntry(section, entry);
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public Properties getSection(final String section) throws Exception {
        try {
            Properties properties = new Properties();
            Pattern p = Pattern.compile("[#]");
            String[] keyValue = null;
            java.util.Enumeration<Object> enuma = entries.keys();
            while (enuma.hasMoreElements()) {
                keyValue = p.split(enuma.nextElement().toString());
                if (getSectionEntry(section, keyValue[1]) != null) {
                    properties.put(normalizeKey(keyValue[1]), this.getSectionEntry(section, keyValue[1]));
                }
            }
            return properties;
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    public Properties getSection(final String section, boolean usePrefix, String propertyPrefix) throws Exception {
        try {
            Properties properties = new Properties();
            Pattern p = Pattern.compile("[#]");
            String[] keyValue = null;
            java.util.Enumeration<Object> enuma = entries.keys();
            while (enuma.hasMoreElements()) {
                keyValue = p.split(enuma.nextElement().toString());
                if (getSectionEntry(section, keyValue[1]) != null) {
                    properties.put(normalizeKey(propertyPrefix+keyValue[1]), this.getSectionEntry(section, keyValue[1]));
                }
            }
            return properties;
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public Properties getSection() throws Exception {
        try {
            return this.getSection(section);
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public Properties getSection(final String application, final String section) throws Exception {
        try {
            return this.getSection(this.section);
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public ArrayList<String> getSections() throws Exception {
        return sections;
    }

    synchronized public int getSequence(final String section, final String entry) throws Exception {
        String sectionName = null;
        String line = null;
        File inFile = null;
        File outFile = null;
        OutputStream out = null;
        BufferedReader in = null;
        int result = -1;
        try {
            inFile = new File(source);
            if (!inFile.exists()) {
                throw new Exception("couldn't find profile [" + source + "].");
            }
            outFile = new File(source + "~");
            out = new FileOutputStream(outFile);
            in = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                Matcher sectionMatcher = SECTION_PATTERN.matcher(line);
                Matcher entryMatcher = ENTRY_PATTERN.matcher(line);
                if (sectionMatcher.matches()) {
                    sectionName = sectionMatcher.group(1);
                    out.write(line.getBytes());
                } else if (entryMatcher.matches()) {
                    String name = entryMatcher.group(1).trim();
                    String value = entryMatcher.group(2).trim();
                    if (name.equals(entry) && sectionName.equals(section) && value.matches("[0-9]+")) {
                        result = Integer.valueOf(value.trim()).intValue() + 1;
                        out.write(new String(entry + "=" + result).getBytes());
                    } else {
                        out.write(line.getBytes());
                    }
                } else {
                    out.write(line.getBytes());
                }
                out.write(10);
                out.flush();
            }
        } catch (Exception e) {
            try {
                outFile.delete();
            } catch (Exception ex) {
                //
            }
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        } finally {
            if (outFile != null) {
                try {
                    outFile.renameTo(inFile);
                } catch (Exception ex) {
                    // no exception handling
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    // no exception handling
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    // no exception handling
                }
            }
            this.load();
        }
        return result;
    }

    @Override
    public void setKeysToLowerCase() throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        LOGGER.debug(".. now keys set to lower case.");
        lowerCase = true;
    }

    @Override
    public void setKeysToUpperCase() throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        LOGGER.debug(".. now keys set to upper case.");
        lowerCase = false;
    }

    protected String normalizeKey(final String key) throws Exception {
        try {
            if (SOSString.isEmpty(key)) {
                throw new Exception(SOSClassUtil.getMethodName() + ": invalid key.");
            }
            if (this.getIgnoreCase()) {
                return key;
            }
            if (lowerCase) {
                return key.toLowerCase();
            } else {
                return key.toUpperCase();
            }
        } catch (Exception e) {
            throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
        }
    }

    @Override
    public void setIgnoreCase(final boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean getIgnoreCase() {
        return ignoreCase;
    }

}