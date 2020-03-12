package sos.settings.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;

/** @author Robert Ehrlich */
public class SOSSettingsProfile2XMLConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSSettingsProfile2XMLConverter.class);
    private String source = "";
    private String xmlEncoding = "ISO-8859-1";
    private String xmlStylesheet = "";
    private static final Pattern SECTION_PATTERN = Pattern.compile("^\\s*\\[([^\\]]*)\\].*$");
    private static final Pattern ENTRY_PATTERN = Pattern.compile("^([; a-z A-Z 0-9_]+)[ \t\n]*=(.*)$");
    private static final String INDENT = "  ";
    private String newLine = System.getProperty("line.separator");
    private Map<String, Map<String, String>> sections = new LinkedHashMap<String, Map<String, String>>();
    private Map<String, List<String>> entryNotes = new LinkedHashMap<String, List<String>>();

    public SOSSettingsProfile2XMLConverter(String source) throws Exception {
        this.source = source;
        this.load();
    }

    private void load() throws Exception {
        String sectionName = null;
        BufferedReader in = null;
        String key = null;
        String value = null;
        String line = null;
        Map<String, String> entries = null;
        List<String> notes = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                throw new Exception("couldn't find source [" + source + "].");
            }
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            sections = new LinkedHashMap<String, Map<String, String>>();
            entries = new LinkedHashMap<String, String>();
            entryNotes = new LinkedHashMap<String, List<String>>();
            notes = new ArrayList<String>();
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                Matcher matcher = SECTION_PATTERN.matcher(line);
                if (matcher.matches()) {
                    entries = new LinkedHashMap<String, String>();
                    notes = new ArrayList<String>();
                    sectionName = matcher.group(1);
                } else {
                    matcher = ENTRY_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        key = matcher.group(1).trim();
                        value = matcher.group(2).trim();
                        entries.put(key, value);
                        if (!notes.isEmpty()) {
                            entryNotes.put(key, notes);
                            notes = new ArrayList<String>();
                        }
                    } else {
                        line = line.trim();
                        if (line.startsWith(";") && line.length() > 1) {
                            notes.add(line.substring(1).trim());
                        }
                    }
                }
                sections.put(sectionName, entries);
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": profile [" + source + "] successfully loaded.");
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

    public void process(String application, String xmlFilename) throws Exception {
        FileWriter f = new FileWriter(xmlFilename);
        f.write("<?xml version=\"1.0\" encoding=\"" + this.xmlEncoding + "\" ?>" + newLine);
        if (this.xmlStylesheet != null && !this.xmlStylesheet.isEmpty()) {
            f.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + this.xmlStylesheet + "\" ?>" + newLine);
            f.write(newLine);
        }
        f.write("<settings>" + newLine);
        f.write(newLine);
        f.write(INDENT + "<application name=\"" + application + "\">" + newLine);
        f.write(this.writeNote("de", "", INDENT + INDENT));
        f.write(this.writeNote("en", "", INDENT + INDENT));
        f.write(newLine);
        f.write(INDENT + INDENT + "<sections>" + newLine);
        for (Map.Entry<String, Map<String, String>> secMapEntry : sections.entrySet()) {
            String sectionName = secMapEntry.getKey();
            Map<String, String> entries = secMapEntry.getValue();
            f.write(INDENT + INDENT + INDENT + "<section name=\"" + sectionName + "\">" + newLine);
            f.write(this.writeNote("de", "", INDENT + INDENT + INDENT + INDENT));
            f.write(this.writeNote("en", "", INDENT + INDENT + INDENT + INDENT));
            f.write(newLine);
            f.write(INDENT + INDENT + INDENT + INDENT + "<entries>" + newLine);
            for (Map.Entry<String, String> entMapEntry : entries.entrySet()) {
                String entryName = entMapEntry.getKey();
                String entryValue = entMapEntry.getValue();
                String disabled = "";
                String noteValue = "";
                if (entryName.startsWith(";")) {
                    entryName = entryName.substring(1);
                    disabled = "disabled=\"true\"";
                }
                f.write(INDENT + INDENT + INDENT + INDENT + INDENT + "<entry name=\"" + entryName + "\" " + disabled + ">" + newLine);
                f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + "<value>" + newLine);
                f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + this.writeCDATA(entryValue) + newLine);
                f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + "</value>" + newLine);
                if (entryNotes != null && entryNotes.containsKey(entryName)) {
                    List<String> notes = entryNotes.get(entryName);
                    int notesSize = notes.size();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < notesSize; i++) {
                        String before = (i > 0) ? INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT
                                : "";
                        String after = (i == (notesSize - 1)) ? "" : newLine;
                        sb.append(before).append(notes.get(i)).append(after);
                    }
                    noteValue = sb.toString();
                }
                f.write(this.writeNote("de", noteValue, INDENT + INDENT + INDENT + INDENT + INDENT + INDENT));
                f.write(this.writeNote("en", noteValue, INDENT + INDENT + INDENT + INDENT + INDENT + INDENT));
                f.write(INDENT + INDENT + INDENT + INDENT + INDENT + "</entry>" + newLine);
                f.write(newLine);
            }
            f.write(INDENT + INDENT + INDENT + INDENT + "</entries>" + newLine);
            f.write(INDENT + INDENT + INDENT + "</section>" + newLine);
            f.write(newLine);
        }
        f.write(INDENT + INDENT + "</sections>" + newLine);
        f.write(newLine);
        f.write(INDENT + "</application>" + newLine);
        f.write(newLine);
        f.write("</settings>");
        f.close();
    }

    private String writeNote(String language, String value, String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("<note language=\"").append(language).append("\">").append(newLine).append(indent).append(INDENT).append(
                "<div xmlns=\"http://www.w3.org/1999/xhtml\">").append(newLine).append(indent).append(INDENT).append(INDENT).append("<p>").append(
                        newLine).append(indent).append(INDENT).append(INDENT).append(INDENT).append(this.writeCDATA(value)).append(newLine).append(
                                indent).append(INDENT).append(INDENT).append("</p>").append(newLine).append(indent).append(INDENT).append("</div>")
                .append(newLine).append(indent).append("</note>").append(newLine);
        return sb.toString();
    }

    private String writeCDATA(String val) {
        return "<![CDATA[" + val + "]]>";
    }

    public String getXMLEncoding() {
        return xmlEncoding;
    }

    public void setXMLEncoding(String encoding) {
        this.xmlEncoding = encoding;
    }

    public String getXmlStylesheet() {
        return xmlStylesheet;
    }

    public void setXmlStylesheet(String xmlStylesheet) {
        this.xmlStylesheet = xmlStylesheet;
    }

}
